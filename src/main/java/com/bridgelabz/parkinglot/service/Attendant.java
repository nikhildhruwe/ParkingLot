package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.observer.ParkingLotOwner;

import java.util.ArrayList;

public class Attendant {
    ParkingLotOwner parkingLotOwner = new ParkingLotOwner();

    public ArrayList<Vehicle> parkVehicle(ArrayList<Vehicle> parkingList, Vehicle vehicle) {
        int slotKey = parkingLotOwner.getSlotKey(parkingList);
        parkingList.add(slotKey, vehicle);
        return parkingList;
    }

}
