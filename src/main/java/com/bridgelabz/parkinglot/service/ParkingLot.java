package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.util.ArrayList;


public class ParkingLot {
    private final int maxCapacity;
    private ArrayList<Vehicle> parkingList;

    private final ArrayList<ParkingLotObserver> observerList;
    private final Attendant attendant;
    private int currentCapacity = 0;

    public ParkingLot(int capacity) {
        this.parkingList = new ArrayList<>();
        this.observerList = new ArrayList<>();
        this.attendant = new Attendant();
        this.maxCapacity = capacity;
    }

    public void addObserver(ParkingLotObserver observer) {
        observerList.add(observer);
    }

    public void parkVehicle(Vehicle vehicle) throws ParkingLotException {
        if (parkingList.contains(vehicle))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (currentCapacity < maxCapacity) {
            parkingList = attendant.parkVehicle(parkingList, vehicle);
            this.currentCapacity++;
        } else if (currentCapacity == maxCapacity) {
            throw new ParkingLotException("Parking Capacity is full",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (currentCapacity == maxCapacity)
            this.notifyAllObservers(true);
    }

    public int getSlotNumber(Vehicle vehicle) {
        return parkingList.indexOf(vehicle) + 1;
    }

    public void unParkVehicle(Vehicle vehicle) {
        if (parkingList.contains(vehicle)) {
            {
                int slotNumber = parkingList.indexOf(vehicle);
                parkingList.set(slotNumber, null);
                this.currentCapacity--;
            }
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
}