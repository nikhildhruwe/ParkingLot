package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ParkingSlotDetails {
    private Vehicle vehicle;
    private LocalDateTime time;

    public ParkingSlotDetails(Vehicle vehicle) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    public ParkingSlotDetails() {
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
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return Objects.equals(vehicle, that.vehicle);
    }
}
