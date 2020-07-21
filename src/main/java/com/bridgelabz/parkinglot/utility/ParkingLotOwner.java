package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean parking;

    public boolean getParkingCapacity() {
        return parking;
    }

    @Override
    public void setParkingCapacity(boolean parking) {
        this.parking = parking;
    }

    public int getSlotKey(ArrayList<Vehicle> parkingList) {
        int slotKey = 0;
        for (Vehicle vehicle : parkingList) {
            if (vehicle == null)
                return slotKey;
            slotKey++;
        }
        return  slotKey;
    }
}
