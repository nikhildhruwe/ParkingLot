package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleCompany;
import com.bridgelabz.parkinglot.enums.VehicleSize;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Car;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    private final int numberOfLots;
    private final int capacity;
    private final ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int numberOfLots, int capacity) {
        this.numberOfLots = numberOfLots;
        this.capacity = capacity;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots).forEach(i -> parkingLotList.add(new ParkingLot(capacity)));
    }

    public void parkVehicle(Car car, String attendant) {
        boolean isPresent = parkingLotList.stream().anyMatch(slot -> slot.isVehicleParked(car));
        if (isPresent)
            throw new ParkingLotException("Vehicle Already Present", ParkingLotException.ExceptionType.ALREADY_PRESENT);
        ParkingLot parkingLot = this.getParkingLot(car);
        parkingLot.parkVehicle(car, attendant);
    }

    private ParkingLot getParkingLot(Car car) {
        ArrayList<ParkingLot> sortedList = new ArrayList<>(parkingLotList);
        ParkingLot parkingLot = null;
        if (car.getDriverType().equals(DriverType.NORMAL)) {
            sortedList.sort(Comparator.comparing(ParkingLot::getVehicleCount));
            return sortedList.get(0);
        }
        if (car.getDriverType().equals(DriverType.HANDICAP)) {
            if (car.getSize().equals(VehicleSize.LARGE)) {
                sortedList.sort(Comparator.comparing(ParkingLot::getVehicleCount));
                return sortedList.get(0);
            }
            return sortedList.stream().filter(lot -> lot.getVehicleCount() < capacity).findFirst().get();
        }
        return parkingLot;
    }

    public void unParkVehicle(Car car) {
        parkingLotList.stream().
                filter(parkingLot -> parkingLot.isVehicleParked(car)).
                findAny().
                ifPresentOrElse(slot -> slot.unParkVehicle(car), () ->
                {
                    throw new ParkingLotException(" No vehicle Present", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
                });
    }

    private ParkingLot getVehicleLocation(Car car) {
        return parkingLotList.stream()
                .filter(parkingLot -> parkingLot.isVehicleParked(car))
                .findFirst().get();
    }

    public int getVehicleLotNumber(Car car) {
        ParkingLot vehicleLocation = this.getVehicleLocation(car);
        return parkingLotList.indexOf(vehicleLocation) + 1;
    }

    public int getVehicleSlotNumber(Car car) {
        ParkingLot vehicleLocation = this.getVehicleLocation(car);
        return vehicleLocation.getVehicleSlotNumber(car) + 1;
    }

    public List<String> getVehicleByColor(String vehicleColor) {
        List<String> vehicleLocationList = new ArrayList<>();
        parkingLotList.stream().map(lot -> lot.parkingSlotList.stream().
                filter(slotNumber -> slotNumber.getCar() != null && slotNumber.getCar().getColor().equals(vehicleColor)).
                collect(Collectors.toList())).
                forEachOrdered(lot -> lot.stream().map(slotDetails -> this.getVehicleLotNumber(slotDetails.getCar())
                        + "-" + this.getVehicleSlotNumber(slotDetails.getCar())).forEach(vehicleLocationList::add));

        if (vehicleLocationList.size() == 0)
            throw new ParkingLotException("Given Color Not Present", ParkingLotException.ExceptionType.INVALID_COLOR);
        return vehicleLocationList;
    }

    public List<String> getVehicleDetailsByCompanyAndColor(VehicleCompany company, String color) {
        List<String> vehicleDetails = new ArrayList<>();
        for (int slots = 0; slots < capacity; slots++)
            for (int lots = 0; lots < numberOfLots; lots++) {
                Car car = parkingLotList.get(lots).parkingSlotList.get(slots).getCar();
                if (car != null && car.getCompany().equals(company) && car.getColor().equals(color))
                    vehicleDetails.add("Lot: " + this.getVehicleLotNumber(car)
                            + ",Slot: " + this.getVehicleSlotNumber(car)
                            + ",Attendant: " + parkingLotList.get(lots).parkingSlotList.get(slots).getAttendant()
                            + ",Number Plate: " + car.getNumberPlate());
            }
        if (vehicleDetails.size() == 0)
            throw new ParkingLotException("No Such Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }

    public List<String> getVehicleDetailsByCompany(VehicleCompany company) {
        List<String> vehicleDetails = new ArrayList<>();
        for (int slots = 0; slots < capacity; slots++)
            for (int lots = 0; lots < numberOfLots; lots++) {
                Car car = parkingLotList.get(lots).parkingSlotList.get(slots).getCar();
                if (car != null && car.getCompany().equals(company))
                    vehicleDetails.add("Lot: " + this.getVehicleLotNumber(car)
                            + ",Slot: " + this.getVehicleSlotNumber(car)
                            + ",Number Plate: " + car.getNumberPlate());
            }
        if (vehicleDetails.size() == 0)
            throw new ParkingLotException("No Such Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }

    public List<String> getVehicleDetailsWithInProvidedTime(int minutes) {
        List<String> vehicleDetails = new ArrayList<>();
        for (int slots = 0; slots < capacity; slots++)
            for (int lots = 0; lots < numberOfLots; lots++) {
                Car car = parkingLotList.get(lots).parkingSlotList.get(slots).getCar();
                if (car != null && parkingLotList.get(lots).getParkingDuration(car) <= minutes)
                    vehicleDetails.add("Lot: " + this.getVehicleLotNumber(car)
                            + ",Slot: " + this.getVehicleSlotNumber(car)
                            + ",Number Plate: " + car.getNumberPlate());
            }
        if (vehicleDetails.isEmpty())
            throw new ParkingLotException("No Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }

    public List<String> getVehicleDetailsOfHandicapCarFromGivenLot(int lotNumber) {
        List<String> vehicleDetails;
        if (lotNumber > numberOfLots)
            throw new ParkingLotException("Lot Number Does Not Exist", ParkingLotException.ExceptionType.INVALID_LOT);
        vehicleDetails = IntStream.range(0, capacity).mapToObj(slot -> parkingLotList.get(lotNumber - 1).
                parkingSlotList.get(slot).getCar()).filter(car -> car != null && car.getSize().equals(VehicleSize.SMALL) &&
                car.getDriverType().equals(DriverType.HANDICAP))
                .map(car -> "Lot: " + this.getVehicleLotNumber(car)
                        + ",Slot: " + this.getVehicleSlotNumber(car)
                        + ",Number Plate: " + car.getNumberPlate()).collect(Collectors.toList());
        if (vehicleDetails.isEmpty())
            throw new ParkingLotException("No Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }

    public List<String> getVehicleDetailsOfAParkingLot(int lotNumber) {
        List<String> vehicleDetails;
        if (lotNumber > numberOfLots)
            throw new ParkingLotException("Lot Number Does Not Exist", ParkingLotException.ExceptionType.INVALID_LOT);
        vehicleDetails = IntStream.range(0, capacity).
                mapToObj(slot -> parkingLotList.get(lotNumber - 1).parkingSlotList.get(slot).getCar()).
                filter(Objects::nonNull).
                map(car -> "Lot: " + this.getVehicleLotNumber(car) + ",Slot: " + this.getVehicleSlotNumber(car)
                        + ",Number Plate: " + car.getNumberPlate()).collect(Collectors.toList());
        if (vehicleDetails.isEmpty())
            throw new ParkingLotException("No Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }
}
