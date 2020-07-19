package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.enums.VIEWER;
import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.utility.AirportSecurity;
import com.bridgelabz.parkinglot.utility.ParkingLotOwner;

import java.util.ArrayList;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<Vehicle> parkingList;

    AirportSecurity airportSecurity = new AirportSecurity();
    ParkingLotOwner parkingLotOwner =new ParkingLotOwner();
    public ParkingLot() {
        this.parkingList = new ArrayList<>();
    }

    /**
     * Method to park vehicle to parking lot
     * @throws ParkingLotException already present in parking lot
     */
    public void parkVehicle(Vehicle vehicle) throws ParkingLotException {
        if (parkingList.contains(vehicle))
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.ALREADY_PRESENT);
        if (parkingList.size() < MAX_CAPACITY)
            parkingList.add(vehicle);
        else if (parkingList.size() == MAX_CAPACITY) {
            throw new ParkingLotException("Present in parking lot",
                    ParkingLotException.ExceptionType.CAPACITY_EXCEEDED);
        }
        if (parkingList.size() == MAX_CAPACITY) {
            airportSecurity.setParkingStatus(true);
            parkingLotOwner.setParkingAvailability(true);
        }
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        return parkingList.contains(vehicle);
    }

    public boolean isVehicleUnParked(Vehicle vehicle) {
        return !parkingList.contains(vehicle);
    }

    public boolean isParkingFull(VIEWER viewer) {
        boolean parkingStatus = false;
        switch (viewer){
            case OWNER:
                 parkingStatus = parkingLotOwner.getParkingStatus();
                break;
            case AIRPORT_SECURITY:
                 parkingStatus = airportSecurity.getParkingStatus();
        }
        return parkingStatus;
    }

    /**
     * Method to unPark vehicle if present
     *
     * @param vehicle
     * @return return true or false accordingly
     */
    public void unParkVehicle(Vehicle vehicle) {
        if (parkingList.contains(vehicle)) {
            parkingList.remove(vehicle);
            parkingLotOwner.setParkingAvailability(true);
        }
    }
}
