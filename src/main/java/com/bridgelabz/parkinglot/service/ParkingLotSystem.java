package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.IntStream;

public class ParkingLotSystem {
    private final int numberOfLots;
    public final int capacity;
    public ArrayList<ParkingLot> parkingLotList;

    public ParkingLotSystem(int numberOfLots, int capacity) {
        this.numberOfLots = numberOfLots;
        this.capacity = capacity;
        this.parkingLotList = new ArrayList<>();
        IntStream.range(0, numberOfLots).forEach(i -> parkingLotList.add(new ParkingLot(capacity)));
    }

    public void parkVehicle(Vehicle vehicle) {
        boolean isPresent = parkingLotList.stream().anyMatch(slot -> slot.isVehicleParked(vehicle));
        if (isPresent)
            throw new ParkingLotException("Vehicle Already Parked", ParkingLotException.ExceptionType.ALREADY_PRESENT);
        ParkingLot parkingLot = this.getParkingLot();
        parkingLot.parkVehicle(vehicle);
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
