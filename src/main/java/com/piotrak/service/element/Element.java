package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;

public abstract class Element {

    private final String name;

    public Element(@NotNull String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void actOnCommand(Command command) throws OperationNotSupportedException;
}
