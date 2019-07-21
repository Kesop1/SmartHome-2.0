package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;

/**
 * Horizontal separator element to be displayed in the GUI
 */
public class HorizontalSeparatorElement extends Element {

    public HorizontalSeparatorElement(@NotBlank String name) {
        super(name);
    }

    /**
     * this element is not interactive
     * @param command Command received
     * @throws OperationNotSupportedException this element is not interactive
     */
    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("This is a display element only");
    }
}
