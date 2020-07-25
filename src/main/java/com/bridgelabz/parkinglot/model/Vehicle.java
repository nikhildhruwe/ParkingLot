package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private VehicleSize size;

    public Vehicle() {
    }

    public Vehicle(VehicleSize size) {
        this.size = size;
    }

    public VehicleSize getSize() {
        return size;
    }
}
