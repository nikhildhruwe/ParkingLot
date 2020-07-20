package com.bridgelabz.parkinglot.utility;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean parking;

    public boolean getParkingCapacity() {
        return parking;
    }

    @Override
    public void setParkingCapacity(boolean parking) {
        this.parking = parking;
    }
}
