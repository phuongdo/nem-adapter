package com.netobjex.ms.model.exception;

public class PermException extends Exception {


    public PermException() {
        super("Permission Denied");
    }

    public PermException(String message) {
        super(message);
    }

    public PermException(String message, Throwable throwable) {
        super(message, throwable);
    }


}
