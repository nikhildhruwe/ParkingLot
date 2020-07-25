package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private VehicleSize size;
    private String color;

    public Vehicle() {
    }

    public Vehicle(VehicleSize size, String color) {
        this.size = size;
        this.color = color;
    }

    public VehicleSize getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }
}
