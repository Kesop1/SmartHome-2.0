package com.piotrak.service;

public class CalendarException extends Exception {

    public CalendarException() {
    }

    public CalendarException(String message) {
        super(message);
    }

    public CalendarException(String message, Throwable cause) {
        super(message, cause);
    }
}
