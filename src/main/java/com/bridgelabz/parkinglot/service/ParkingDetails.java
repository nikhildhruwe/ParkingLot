package com.bridgelabz.parkinglot.service;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ParkingDetails {
    private Vehicle vehicle;
    private LocalDateTime time;

    public ParkingDetails(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    public ParkingDetails() {
    }

    public LocalDateTime getParkedTime()
    {
        return time;
    }

    public Vehicle getVehicle(){
        return vehicle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingDetails that = (ParkingDetails) o;
        return Objects.equals(vehicle, that.vehicle) &&
                Objects.equals(time, that.time);
    }
}
