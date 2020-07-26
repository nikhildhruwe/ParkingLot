package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private DriverType driverType;
    private VehicleSize size;

    public Vehicle() {
    }

    public Vehicle(VehicleSize size, DriverType driverType) {
        this.driverType = driverType;
        this.size = size;
    }

    public VehicleSize getSize() {
        return size;
    }

    public DriverType getDriverType() {
        return driverType;
    }
}
