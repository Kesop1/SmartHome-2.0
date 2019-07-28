package com.piotrak.service.technology;

/**
 * Command for the elements
 */
public abstract class Command {

    /**
     * Value of the command
     */
    private final String value;

    public Command() {
        this.value = "";
    }

    public Command(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract String toString();
}
