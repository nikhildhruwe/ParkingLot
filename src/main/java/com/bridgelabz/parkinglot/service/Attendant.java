package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Attendant {
    ParkingLotOwner parkingLotOwner = new ParkingLotOwner();

    private void initializeList(ArrayList<Vehicle> initialList) {
        if (initialList.isEmpty())
            IntStream.rangeClosed(1, 2).<Vehicle>mapToObj(slot -> null).forEach(initialList::add);
    }

    public ArrayList<Vehicle> parkVehicle(ArrayList<Vehicle> parkingList, Vehicle vehicle) {
        this.initializeList(parkingList);
        int slotKey = parkingLotOwner.getSlotKey(parkingList);
        parkingList.set(slotKey, vehicle);
        return parkingList;
    }
}
