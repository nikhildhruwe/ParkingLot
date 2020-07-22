package com.bridgelabz.parkinglot.observer;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean parking;

    @Override
    public boolean getParkingCapacity() {
        return parking;
    }

    @Override
    public void setParkingCapacity(boolean parking) {
        this.parking = parking;
    }
}
