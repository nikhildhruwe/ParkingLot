package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.utility.AirportSecurity;

import java.util.ArrayList;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<String> parkingList;
    private int capacity = 0;

    public ParkingLot() {
        this.parkingList = new ArrayList<>();
    }

    /**
     * Method for vehicle parking.
     * @return number of parked vehicles
     */
    public int vehicleParking(String[] vehicle) throws ParkingLotException {
        addVehicle(vehicle);
        return parkingList.size();
    }

    /**
     * Method to add vehicle to parking lot
     * @param vehicles
     * @throws ParkingLotException
     */
    private void addVehicle(String[] vehicles) throws ParkingLotException {
        for (String vehicle : vehicles) {
            if (capacity < MAX_CAPACITY)
                parkingList.add(vehicle);
            else {
                this.informOwner();
                return;
              //  throw new ParkingLotException("Capacity Full", ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
            }
            capacity++;
        }
    }

    private void informOwner() {
        new AirportSecurity().parkingFull();
    }

    public boolean isParkingFull(){
        return new AirportSecurity().getParkingStatus();
    }

    /**
     * Method to unPark vehicle if present
     * @return return true or false accordingly
     */
    public boolean vehicleUnparking(String vehicleNumber) {
        if (parkingList.contains(vehicleNumber)) {
            parkingList.remove(vehicleNumber);
         //   new AirportSecurity().parkingAvailable();
            return true;
        }
        return false;
    }
}
