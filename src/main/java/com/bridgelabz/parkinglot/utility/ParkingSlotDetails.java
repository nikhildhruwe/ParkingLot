package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Vehicle;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ParkingSlotDetails {
    private String attendant;
    private Vehicle vehicle;
    private LocalDateTime time;

    public ParkingSlotDetails(Vehicle vehicle, String attendant) {
        this.vehicle = vehicle;
        this.time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
        this.attendant = attendant;
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

    public String getAttendant() {
        return attendant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return Objects.equals(vehicle, that.vehicle);
    }
}
