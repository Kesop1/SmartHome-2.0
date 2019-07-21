package com.piotrak.service.technology;

import javax.validation.constraints.NotBlank;

/**
 * Command for the elements
 */
public abstract class Command {

    /**
     * Value of the command
     */
    private final String value;

    public Command(@NotBlank String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract String toString();
}
