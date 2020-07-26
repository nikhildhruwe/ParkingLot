package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Car {
    private DriverType driverType;
    private VehicleSize size;
    private String color;

    public Car() {
    }

    public Car(VehicleSize size, DriverType driverType, String color) {
        this.color = color;
        this.size = size;
        this.driverType = driverType;
    }

    public VehicleSize getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public DriverType getDriverType() {
        return driverType;
    }
}
