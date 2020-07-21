package com.bridgelabz.parkinglot.observer;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;

public class ParkingLotOwner implements ParkingLotObserver {
    private boolean parking;
    public ArrayList<Boolean> parkingSlotList = new ArrayList<>();

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
        return slotKey;
    }

    private void initializeList(ArrayList<Boolean> parkingSlotList) {
        if (parkingSlotList.isEmpty()) {
            for (int slot = 1; slot <= 2; slot++) {
                Boolean aBoolean = null;
                parkingSlotList.add(aBoolean);
            }
        }
    }

    public void informOwnerAboutParkingSlot(int slotNumber) {
        this.initializeList(parkingSlotList);
        parkingSlotList.set(slotNumber, true);
    }

    public boolean isParkingSlotUsed(int slotNumber) {
        return this.parkingSlotList.get(slotNumber - 1);
    }
}
