package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;

public class HorizontalSeparatorElement extends Element {

    public HorizontalSeparatorElement(@NotNull String name) {
        super(name);
    }

    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        //do nothing
    }
}
