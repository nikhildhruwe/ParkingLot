package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.DriverType;
import com.bridgelabz.parkinglot.enums.VehicleCompany;
import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Car {
    private DriverType driverType;
    private String numberPlate;
    private VehicleCompany company;
    private String color;
    private VehicleSize size;

    public Car() {
    }

    public Car(VehicleSize size, DriverType driverType, String color) {
        this.color = color;
        this.size = size;
        this.driverType = driverType;
    }

    public Car(VehicleSize size, DriverType driverType, VehicleCompany company, String color, String numberPlate) {
        this.color = color;
        this.company = company;
        this.size = size;
        this.numberPlate = numberPlate;
        this.driverType = driverType;
    }

    public VehicleSize getSize() {
        return size;
    }

    public String getColor() {
        return color;
    }

    public VehicleCompany getCompany() {
        return company;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public DriverType getDriverType() {
        return driverType;
    }
}
