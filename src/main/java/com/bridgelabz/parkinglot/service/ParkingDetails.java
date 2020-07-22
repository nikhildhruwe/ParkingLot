package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.time.LocalDateTime;

public class ParkingDetails {
    private final Vehicle vehicle;
    private LocalDateTime time;

    public ParkingDetails(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now();
    }

    public LocalDateTime getParkedTime()
    {
        return time;
    }

    public void setParkedTime(LocalDateTime time)
    {
        this.time = time;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }
}
