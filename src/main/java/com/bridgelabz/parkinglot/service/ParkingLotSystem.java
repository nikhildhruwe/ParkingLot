package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    public final int numberOfLots;
    public final int capacity;
    public ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int numberOfLots, int capacity) {
        this.numberOfLots = numberOfLots;
        this.capacity = capacity;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots).forEach(i -> parkingLotList.add(new ParkingLot(capacity)));
    }

    public void parkVehicle(Vehicle vehicle, DriverType driverType) {
        boolean isPresent = parkingLotList.stream().anyMatch(slot -> slot.isVehicleParked(vehicle));
        if (isPresent)
            throw new ParkingLotException("Vehicle Already Parked", ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (driverType.equals(DriverType.HANDICAP)){
            IntStream.range(0, numberOfLots).filter(index -> parkingLotList.get(index).getVehicleCount() != capacity).
                    findFirst().ifPresent(i -> parkingLotList.get(i).parkVehicle(vehicle));
        }
        if (driverType.equals(DriverType.NORMAL)){
        ParkingLot parkingLot = this.getParkingLot();
        parkingLot.parkVehicle(vehicle);
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

}
