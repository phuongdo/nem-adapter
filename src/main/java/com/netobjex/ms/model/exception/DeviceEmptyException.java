package com.netobjex.ms.model.exception;

public class DeviceEmptyException extends Exception {
    public DeviceEmptyException() {
        super("Empty Device");
    }

    public DeviceEmptyException(String message) {
        super(message);
    }

    public DeviceEmptyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
