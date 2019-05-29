package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SwitchElement extends Element {

    private Logger LOGGER = Logger.getLogger("SwitchElement");
    
    private boolean on = false;

    public SwitchElement(@NotNull String name) {
        super(name);
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
        String cmd = command.getValue();
        if(!"ON".equalsIgnoreCase(cmd) && !"OFF".equalsIgnoreCase(cmd)){
            throw new OperationNotSupportedException("Invalid command: '" + cmd + "' sent for the SwitchElement: " + getName());
        }
        setOn("ON".equalsIgnoreCase(cmd));
    }
}
