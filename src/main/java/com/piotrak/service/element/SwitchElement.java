package com.piotrak.service.element;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotNull;

public class SwitchElement extends Element {
    
    private boolean on = false;

    public SwitchElement(@NotNull String name) {
        super(name);
    }

    public boolean isOn() {
        return on;
    }
    
    public void setOn(boolean on) {
        this.on = on;
    }

    @Override
    public void actOnCommand(Command command) {
        setOn("ON".equals(command.getValue()));
    }
}
