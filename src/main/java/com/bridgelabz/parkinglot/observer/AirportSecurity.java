package com.bridgelabz.parkinglot.observer;

public class AirportSecurity implements  ParkingLotObserver{
    private boolean parkingStatus;

    @Override
    public boolean getParkingCapacity() {
        return parkingStatus;
    }

    @Override
    public void setParkingCapacity(boolean parkingStatus) {
        this.parkingStatus = parkingStatus;
    }
}
