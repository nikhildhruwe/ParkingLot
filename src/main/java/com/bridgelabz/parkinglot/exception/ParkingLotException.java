package com.bridgelabz.parkinglot.exception;

public class ParkingLotException extends RuntimeException {
    public enum ExceptionType {
        CAPACITY_EXCEEDED, ALREADY_PRESENT
    }

    public ExceptionType type;

    public ParkingLotException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
