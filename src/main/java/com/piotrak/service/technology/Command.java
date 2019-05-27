package com.piotrak.service.technology;

public abstract class Command {

    private final String value;

    public Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
