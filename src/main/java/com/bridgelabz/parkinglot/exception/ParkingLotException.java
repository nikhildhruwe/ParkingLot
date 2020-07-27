package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException {
    public enum ExceptionType {
        CAPACITY_EXCEEDED, VEHICLE_NOT_FOUND, INVALID_COLOR, INVALID_DRIVER, ALREADY_PRESENT
    }

    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
