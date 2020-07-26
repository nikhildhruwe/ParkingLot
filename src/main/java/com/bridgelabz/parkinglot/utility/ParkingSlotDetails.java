package com.bridgelabz.parkinglot.utility;

import com.bridgelabz.parkinglot.model.Car;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class ParkingSlotDetails {
    private Car car;
    private LocalDateTime time;

    public ParkingSlotDetails(Car car) {
        this.car = car;
        this.time = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
    }

    public ParkingSlotDetails() {
    }

    public LocalDateTime getParkedTime()
    {
        return time;
    }

    public Car getCar(){
        return car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParkingSlotDetails that = (ParkingSlotDetails) o;
        return Objects.equals(car, that.car);
    }
}
