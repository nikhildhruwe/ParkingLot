package com.bridgelabz.parkinglot.observer;

import com.bridgelabz.parkinglot.service.ParkingDetails;

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

    public int getSlotKey(ArrayList<ParkingDetails> parkingList, ParkingDetails details) {
        int slotKey = 0;
        for (ParkingDetails parkingDetails : parkingList) {
            if (parkingDetails == details)
                return slotKey;
            slotKey++;
        }
        return slotKey;
    }
}
