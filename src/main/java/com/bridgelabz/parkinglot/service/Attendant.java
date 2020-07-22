package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class Attendant {

    public int getSlotKey(ArrayList<ParkingDetails> parkingList, ParkingDetails  details) {
        int slotKey = 0;
        for (ParkingDetails parkingDetails : parkingList) {
            if (parkingDetails == details)
                return slotKey;
            slotKey++;
        }
        return slotKey;
    }

    public ArrayList<ParkingDetails> parkVehicle(ArrayList<ParkingDetails> parkingList, Vehicle vehicle, ParkingDetails parkingDetails) {
        if (parkingList.isEmpty())
            IntStream.range(0, 2).mapToObj(slot -> parkingDetails).forEach(parkingList::add);
        int slotKey = this.getSlotKey(parkingList, parkingDetails);
        parkingList.set(slotKey, new ParkingDetails(vehicle));
        return parkingList;
    }
}
