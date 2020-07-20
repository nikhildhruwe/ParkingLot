package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.utility.ParkingLotOwner;

public class Attendant {
    ParkingLotOwner parkingLotOwner = new ParkingLotOwner();

    public int parkVehicle() {
       return parkingLotOwner.getKey();
    }

}
