package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.utility.AirportSecurity;

import java.util.ArrayList;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<Vehicle> parkingList;

    private int capacity = 0;
    public boolean isParked;

    public ParkingLot() {
        this.parkingList = new ArrayList<>();
    }

    /**
     * Method to add vehicle to parking lot
     * @throws ParkingLotException
     * @return
     */
    public void parkVehicle(Vehicle vehicle) throws ParkingLotException {
            if(this.parkingList.contains(vehicle))
                throw new ParkingLotException("Present in parking lot",
                                                                    ParkingLotException.ExceptionType.ALREADY_PRESENT);

            if (capacity < MAX_CAPACITY) {
                parkingList.add(vehicle);
                isParked = true;
            }
                //this.informOwner();
                //  throw new ParkingLotException("Capacity Full", ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }

    public boolean isVehicleParked() {
        return isParked;
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
     * @param vehicle
     */
    public boolean unParkVehicle(Vehicle vehicle) {
        if (parkingList.contains(vehicle)) {
            parkingList.remove(vehicle);
            isParked = false;
           new AirportSecurity().parkingAvailable();
            return true;
        }
        return false;
    }
}
