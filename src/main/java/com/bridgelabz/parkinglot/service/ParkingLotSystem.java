package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleCompany;
import com.bridgelabz.parkinglot.enums.VehicleSize;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public final int numberOfLots;
    public final int capacity;
    private final ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int numberOfLots, int capacity) {
        this.numberOfLots = numberOfLots;
        this.capacity = capacity;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots).forEach(i -> parkingLotList.add(new ParkingLot(capacity)));
    }

    public void parkVehicle(Vehicle vehicle, DriverType driverType, String attendant) {
        boolean isPresent = parkingLotList.stream().anyMatch(slot -> slot.isVehicleParked(vehicle));
        if (isPresent)
            throw new ParkingLotException("Vehicle Already Present", ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (driverType.equals(DriverType.HANDICAP)){
            if ( (vehicle.getSize() != null) && vehicle.getSize().equals(VehicleSize.LARGE)) {
                ParkingLot parkingLot = this.getParkingLot();
                parkingLot.parkVehicle(vehicle, attendant);
                return;
            }
            IntStream.range(0, numberOfLots).filter(index -> parkingLotList.get(index).getVehicleCount() != capacity).
                    findFirst().ifPresent(i -> parkingLotList.get(i).parkVehicle(vehicle, attendant));
        }
        if (driverType.equals(DriverType.NORMAL)){
        ParkingLot parkingLot = this.getParkingLot();
        parkingLot.parkVehicle(vehicle, attendant);
        }
    }

    public void unParkVehicle(Vehicle vehicle) {
        parkingLotList.stream().
                filter(parkingLot -> parkingLot.isVehicleParked(vehicle)).
                findAny().
                ifPresentOrElse(slot -> slot.unParkVehicle(vehicle), () ->
                { throw new ParkingLotException(" No vehicle Present", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
                });
    }

    private ParkingLot getParkingLot() {
            ArrayList<ParkingLot> sortedList = new ArrayList<>(parkingLotList);
            sortedList.sort(Comparator.comparing(ParkingLot::getVehicleCount));
            return sortedList.get(0);
    }

    private ParkingLot getVehicleLocation(Vehicle vehicle) {
            return parkingLotList.stream()
                .filter(parkingLot -> parkingLot.isVehicleParked(vehicle))
                .findFirst().get();
    }

    public int getVehicleLotNumber(Vehicle vehicle){
        ParkingLot vehicleLocation = this.getVehicleLocation(vehicle);
        return parkingLotList.indexOf(vehicleLocation) + 1;
    }

    public int getVehicleSlotNumber(Vehicle vehicle){
        ParkingLot vehicleLocation = this.getVehicleLocation(vehicle);
        return vehicleLocation.getVehicleSlotNumber(vehicle) + 1;
    }

    public List<String> getVehicleByColor(String vehicleColor) {
        List<String> vehicleLocationList = new ArrayList<>();
        for ( int i = 0; i < numberOfLots ; i++ )
            for (int j = 0; j < capacity; j++) {
                Vehicle vehicle = parkingLotList.get(j).parkingSlotList.get(i).getVehicle();
                if (vehicle != null && vehicle.getColor().equals(vehicleColor))
                    vehicleLocationList.add(this.getVehicleLotNumber(vehicle) + "-" + this.getVehicleSlotNumber(vehicle));
            }
        if(vehicleLocationList.size() == 0)
            throw new ParkingLotException("Given Color Not Present", ParkingLotException.ExceptionType.INVALID_COLOR);
        return vehicleLocationList;
    }

    public List<String> getVehicleDetailsByCompanyAndColor(VehicleCompany company, String color) {
        List<String> vehicleDetails = new ArrayList<>();
        for ( int i = 0; i < numberOfLots ; i++ )
            for (int j = 0; j < capacity; j++) {
                Vehicle vehicle = parkingLotList.get(j).parkingSlotList.get(i).getVehicle();
                if (vehicle != null && vehicle.getCompany().equals(company) && vehicle.getColor().equals(color))
                    vehicleDetails.add("Lot: "+this.getVehicleLotNumber(vehicle)
                            + ",Slot: " + this.getVehicleSlotNumber(vehicle)
                            + ",Attendant: " + parkingLotList.get(j).parkingSlotList.get(i).getAttendant()
                            + ",Number Plate: " + vehicle.getNumberPlate());
            }
        if (vehicleDetails.size() == 0)
            throw new ParkingLotException("No Such Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }

    public List<String> getVehicleDetailsByCompany(VehicleCompany company) {
        List<String> vehicleDetails = new ArrayList<>();
        for (int i = 0; i < numberOfLots; i++)
            for (int j = 0; j < capacity; j++) {
                Vehicle vehicle = parkingLotList.get(j).parkingSlotList.get(i).getVehicle();
                if (vehicle != null && vehicle.getCompany().equals(company))
                    vehicleDetails.add("Lot: " + this.getVehicleLotNumber(vehicle)
                            + ",Slot: " + this.getVehicleSlotNumber(vehicle)
                            + ",Number Plate: " + vehicle.getNumberPlate());
            }
        if (vehicleDetails.size() == 0)
            throw new ParkingLotException("No Such Vehicle Found", ParkingLotException.ExceptionType.VEHICLE_NOT_FOUND);
        return vehicleDetails;
    }
}
