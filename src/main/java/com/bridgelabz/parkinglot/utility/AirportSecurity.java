package com.bridgelabz.parkinglot.utility;

public class AirportSecurity implements  ParkingLotObserver{
    private boolean parkingStatus;

    public boolean getParkingAvailability() {
        return parkingStatus;
    }

    @Override
    public void setParkingAvailability(boolean parkingStatus) {
        this.parkingStatus = parkingStatus;
    }
}
