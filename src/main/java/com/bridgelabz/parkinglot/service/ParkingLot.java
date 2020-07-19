package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.exception.ParkingLotException;
import com.bridgelabz.parkinglot.model.Vehicle;
import com.bridgelabz.parkinglot.utility.AirportSecurity;

import java.util.ArrayList;

public class ParkingLot {

    private static final int MAX_CAPACITY = 2;
    private final ArrayList<Vehicle> parkingList;

    AirportSecurity airportSecurity = new AirportSecurity();

    public ParkingLot() {
        this.parkingList = new ArrayList<>();
    }

    /**
     * Method to park vehicle to parking lot
     *
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
        if (parkingList.size() == MAX_CAPACITY)
            airportSecurity.setParkingStatus(true);
    }

    public boolean isVehicleParked(Vehicle vehicle) {
        return parkingList.contains(vehicle);
    }

    public boolean isVehicleUnParked(Vehicle vehicle) {
        return !parkingList.contains(vehicle);
    }

    public boolean isParkingFull() {
        return airportSecurity.getParkingStatus();
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
            new AirportSecurity().setParkingStatus(false);
        }
    }
}
