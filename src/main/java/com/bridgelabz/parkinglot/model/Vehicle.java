package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private final VehicleSize size;

    public Vehicle() {
        this.size = VehicleSize.SMALL;
    }

    public Vehicle(VehicleSize size) {
        this.size = size;
    }

    public VehicleSize getSize() {
        return size;
    }
}
