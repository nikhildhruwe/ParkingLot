package com.bridgelabz.parkinglot.utility;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean parking;

    public boolean getParkingAvailability() {
        return parking;
    }

    @Override
    public void setParkingAvailability(boolean parking) {
        this.parking = parking;
    }
}
