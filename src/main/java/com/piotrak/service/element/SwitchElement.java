package com.piotrak.service.element;

import com.piotrak.service.technology.Connection;

public class SwitchElement extends Element {
    
    private boolean on = false;
    
    public SwitchElement(Connection connection) {
        super(connection);
    }

    public boolean isOn() {
        return on;
    }
    
    public void setOn(boolean on) {
        this.on = on;
    }
}
