package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;

import java.util.ArrayList;

public class Attendant {
    ParkingLotOwner parkingLotOwner = new ParkingLotOwner();

    private void initializeList(ArrayList<Vehicle> initialList) {
        if (initialList.isEmpty()) {
            for (int slot = 1; slot <= 2; slot++) {
                initialList.add(null);
            }
        }
    }

    public ArrayList<Vehicle> parkVehicle(ArrayList<Vehicle> parkingList, Vehicle vehicle) {
        this.initializeList(parkingList);
        int slotKey = parkingLotOwner.getSlotKey(parkingList);
        parkingList.set(slotKey, vehicle);
        return parkingList;
    }

}
