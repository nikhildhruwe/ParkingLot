package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;

import com.bridgelabz.parkinglot.utility.ParkingLotObserver;


import java.util.ArrayList;
import java.util.Arrays;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<Vehicle> parkingList;

    private final ArrayList<ParkingLotObserver> observerList;
    private final Attendant attendant;
    public int key;
    public Vehicle[] parkingSlot;

    public ParkingLot() {
        this.parkingList = new ArrayList<>();
        this.observerList = new ArrayList<>();
        this.attendant = new Attendant();
        this.parkingSlot = new Vehicle[MAX_CAPACITY];
    }

    public void addObserver(ParkingLotObserver observer) {
        observerList.add(observer);
    }

    public void parkVehicle(Vehicle vehicle) throws ParkingLotException {
        if (parkingList.contains(vehicle))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (parkingList.size() < MAX_CAPACITY) {
            parkingList.add(vehicle);
            key = attendant.parkVehicle();
            this.parkingSlot[key - 1] = vehicle;
        } else if (parkingList.size() == MAX_CAPACITY) {
            throw new ParkingLotException("Parking Capacity is full",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (parkingList.size() == MAX_CAPACITY)
            this.notifyAllObservers(true);
    }

    public int getSlotNumber(Vehicle vehicle) {
        return Arrays.asList(parkingSlot).indexOf(vehicle) + 1;
    }

    public void unParkVehicle(Vehicle vehicle) {
        if (parkingList.contains(vehicle)) {
            parkingList.remove(vehicle);
            this.notifyAllObservers(false);
        }
    }

    private void notifyAllObservers(boolean parkingStatus) {
        for (ParkingLotObserver observer : observerList)
            observer.setParkingCapacity(parkingStatus);
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        return parkingList.contains(vehicle);
    }

    public void unParkVehicle(int slotNumber) {
        Vehicle vehicle = parkingSlot[slotNumber - 1];
        this.unParkVehicle(vehicle);
    }
}
