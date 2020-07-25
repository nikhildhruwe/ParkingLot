package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private String color;
    private VehicleSize size;

    public Vehicle() {
    }

    public Vehicle(VehicleSize size, String color) {
        this.color = color;
        this.size = size;
    }

    public VehicleSize getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }
}
