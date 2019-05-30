package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;

public class HorizontalSeparatorElement extends Element {

    public HorizontalSeparatorElement(@NotBlank String name) {
        super(name);
    }

    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("This is a display element only");
    }
}
