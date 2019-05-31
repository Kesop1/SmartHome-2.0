package com.piotrak.service.element;

import com.piotrak.service.action.Switchable;
import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwitchElement extends Element implements Switchable {

    private Logger LOGGER = Logger.getLogger("SwitchElement");
    
    private boolean on = false;

    public SwitchElement(@NotBlank String name) {
        super(name);
    }

    public SwitchElement(@NotBlank String name, String displayName) {
        super(name, displayName);
    }

    public boolean isOn() {
        return on;
    }
    
    public void setOn(boolean on) {
        LOGGER.log(Level.INFO, getName() + " has been switched " + (on ? "on" : "off"));
        this.on = on;
    }

    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        switchElement(this, command.getValue());
    }
}
