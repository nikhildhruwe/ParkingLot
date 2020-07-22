package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class ParkingLot {

    private final int maxCapacity;
    private ArrayList<ParkingDetails> parkingList;


    ParkingDetails parkingDetails = new ParkingDetails();
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
        if (this.isVehicleParked(vehicle))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (currentCapacity < maxCapacity) {
            parkingList = attendant.parkVehicle(parkingList, vehicle, parkingDetails);
            this.currentCapacity++;
        } else if (currentCapacity == maxCapacity) {
            throw new ParkingLotException("Parking Capacity is full",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (currentCapacity == maxCapacity)
            this.notifyAllObservers(true);
    }

    public int getSlotNumber(Vehicle vehicle) {
        ParkingDetails parkingDetails = parkingList.stream().filter(slot -> slot.getVehicle().equals(vehicle)).findFirst().get();
        return parkingList.indexOf(parkingDetails);
    }

    public void unParkVehicle(Vehicle vehicle) {
        if (this.isVehicleParked(vehicle)) {
            {
                int slotNumber = this.getSlotNumber(vehicle);
                parkingList.set(slotNumber, parkingDetails);
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
        return parkingList.stream().anyMatch(slot -> slot.getVehicle() == vehicle);
    }

    public void unParkVehicle(int slotNumber) {
        ParkingDetails parkingDetails = parkingList.get(slotNumber);
        this.unParkVehicle(parkingDetails.getVehicle());
    }

    public LocalDateTime getParkTime(Vehicle vehicle) {
        return parkingList.get(this.getSlotNumber(vehicle)).getParkedTime();
    }
}
