package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.utility.ParkingSlotDetails;
import com.bridgelabz.parkinglot.model.Car;
import com.bridgelabz.parkinglot.observer.ParkingLotObserver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class ParkingLot {

    private final int maxCapacity;
    public final ArrayList<ParkingSlotDetails> parkingSlotList;

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
            IntStream.range(0, maxCapacity).mapToObj(slot -> parkingSlotDetails).forEach(parkingSlotList::add);
    }

    public void addObserver(ParkingLotObserver observer) {
        observerList.add(observer);
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public void parkVehicle(Car car, String attendant) throws ParkingLotException {
        if (this.isVehicleParked(car))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (currentCapacity < maxCapacity) {
            int slotKey = this.getSlotToParkVehicle(parkingSlotList);
            parkingSlotList.set(slotKey, new ParkingSlotDetails(car, attendant));
            this.currentCapacity++;
            this.vehicleCount++;
        } else if (currentCapacity == maxCapacity) {
            throw new ParkingLotException("Parking Capacity is full",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (currentCapacity == maxCapacity)
            this.notifyAllObservers(true);
    }

    public int getSlotToParkVehicle(ArrayList<ParkingSlotDetails> parkingSlotList) {
        int slotKey = 0;
        for (ParkingSlotDetails parkingDetail : parkingSlotList) {
            if (parkingDetail == this.parkingSlotDetails)
                return slotKey;
            slotKey++;
        }
        return slotKey;
    }

    public int getVehicleSlotNumber(Car car) {
        ParkingSlotDetails parkingSlotDetails = parkingSlotList.stream()
                .filter(slot -> slot.getCar().equals(car)).findFirst().get();
        return parkingSlotList.indexOf(parkingSlotDetails);
    }

    public void unParkVehicle(Car car) {
        if (this.isVehicleParked(car)) {
            {
                int slotNumber = this.getVehicleSlotNumber(car);
                parkingSlotList.set(slotNumber, parkingSlotDetails);
                this.currentCapacity--;
                this.vehicleCount--;
            }
            this.notifyAllObservers(false);
        }
    }

    private void notifyAllObservers(boolean parkingStatus) {
        for (ParkingLotObserver observer : observerList)
            observer.setParkingCapacity(parkingStatus);
    }

    public boolean isVehicleParked(Car car) {
        return parkingSlotList.stream().anyMatch(slot -> slot.getCar() == car);
    }

    public void unParkVehicle(int slotNumber) {
        ParkingSlotDetails parkingSlotDetails = parkingSlotList.get(slotNumber);
        this.unParkVehicle(parkingSlotDetails.getCar());
    }

    public LocalDateTime getParkTime(Car car) {
        return parkingSlotList.get(this.getVehicleSlotNumber(car)).getParkedTime();
    }
}
