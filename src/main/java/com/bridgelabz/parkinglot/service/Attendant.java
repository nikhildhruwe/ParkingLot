package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Attendant {
    ParkingLotOwner parkingLotOwner = new ParkingLotOwner();


    private void initializeList(ArrayList<ParkingDetails> initialList, ParkingDetails parkingDetails) {
        if (initialList.isEmpty())
            IntStream.range(0, 2).mapToObj(slot -> parkingDetails).forEach(initialList::add);
    }

    public ArrayList<ParkingDetails> parkVehicle(ArrayList<ParkingDetails> parkingList, Vehicle vehicle, ParkingDetails parkingDetails) {
        this.initializeList(parkingList, parkingDetails);
        int slotKey = parkingLotOwner.getSlotKey(parkingList, parkingDetails);
        parkingList.set(slotKey, new ParkingDetails(vehicle));
        return parkingList;
    }
}
