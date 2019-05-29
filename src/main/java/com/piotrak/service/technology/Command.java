package com.piotrak.service.technology;

import javax.validation.constraints.NotBlank;

public abstract class Command {

    private final String value;

    public Command(@NotBlank String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public abstract String toString();
}
