package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.ParkingSlotDetails;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParkingLot {

    private final int maxCapacity;
    private final ArrayList<ParkingSlotDetails> parkingSlotList;

    ParkingSlotDetails parkingSlotDetails = new ParkingSlotDetails();
    private final ArrayList<ParkingLotObserver> observerList;
    private int currentCapacity = 0;
    private int vehicleCount;

    public ParkingLot(int capacity) {
        this.parkingSlotList = new ArrayList<>();
        this.observerList = new ArrayList<>();
        this.maxCapacity = capacity;
        this.initialise(parkingSlotList);
    }

    private void initialise(ArrayList<ParkingSlotDetails> parkingSlotList) {
        if (parkingSlotList.isEmpty())
            IntStream.range(0, 2).mapToObj(slot -> parkingSlotDetails).forEach(parkingSlotList::add);
    }

    public void addObserver(ParkingLotObserver observer) {
        observerList.add(observer);
    }

    public int getVehicleCount(){
        return vehicleCount;
    }
    public void parkVehicle(Vehicle vehicle) throws ParkingLotException {
        if (this.isVehicleParked(vehicle))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (currentCapacity < maxCapacity) {
            int slotKey = this.getSlotKey(parkingSlotList);
            parkingSlotList.set(slotKey, new ParkingSlotDetails(vehicle));
            this.currentCapacity++;
            this.vehicleCount++;
        } else if (currentCapacity == maxCapacity) {
            throw new ParkingLotException("Parking Capacity is full",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (currentCapacity == maxCapacity)
            this.notifyAllObservers(true);
    }

    public int getSlotKey(ArrayList<ParkingSlotDetails> parkingSlotList) {
        int slotKey = 0;
        for (ParkingSlotDetails parkingDetail : parkingSlotList) {
            if (parkingDetail == this.parkingSlotDetails)
                return slotKey;
            slotKey++;
        }
        return slotKey;
    }

    public int getVehicleSlotNumber(Vehicle vehicle) {
        ParkingSlotDetails parkingSlotDetails = parkingSlotList.stream()
                                                .filter(slot -> slot.getVehicle().equals(vehicle)).findFirst().get();
        return parkingSlotList.indexOf(parkingSlotDetails);
    }

    public void unParkVehicle(Vehicle vehicle) {
        if (this.isVehicleParked(vehicle)) {
            {
                int slotNumber = this.getVehicleSlotNumber(vehicle);
                parkingSlotList.set(slotNumber, parkingSlotDetails);
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
        return parkingSlotList.stream().anyMatch(slot -> slot.getVehicle() == vehicle);
    }

    public void unParkVehicle(int slotNumber) {
        ParkingSlotDetails parkingSlotDetails = parkingSlotList.get(slotNumber);
        this.unParkVehicle(parkingSlotDetails.getVehicle());
    }

    public LocalDateTime getParkTime(Vehicle vehicle) {
        return parkingSlotList.get(this.getVehicleSlotNumber(vehicle)).getParkedTime();
    }
}
