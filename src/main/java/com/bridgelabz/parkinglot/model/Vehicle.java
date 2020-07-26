package com.bridgelabz.parkinglot.model;

import com.bridgelabz.parkinglot.enums.VehicleCompany;
import com.bridgelabz.parkinglot.enums.VehicleSize;

public class Vehicle {
    private VehicleCompany company;
    private String color;
    private VehicleSize size;

    public Vehicle() {
    }

    public Vehicle(VehicleSize size, String color) {
        this.color = color;
        this.size = size;
    }

    public Vehicle(VehicleSize size, VehicleCompany company, String color) {
        this.color = color;
        this.company = company;
        this.size = size;
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
}
