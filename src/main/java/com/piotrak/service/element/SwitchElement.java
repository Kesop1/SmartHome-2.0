package com.piotrak.service.element;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;

public class SwitchElement extends Element {
    
    private boolean on = false;
    
    public SwitchElement(Connection connection) {
        super(connection);
    }
    
    @Override
    public void sendCommand(Command command) {
        try {
            getConnection().send(command);
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public Command receiveCommand() {
        Command command = null;
        try {
            command = getConnection().receive();
        } catch (ConnectionException e) {
            e.printStackTrace();
        }
        return command;
    }

    public boolean isOn() {
        return on;
    }
    
    public void setOn(boolean on) {
        this.on = on;
    }
}
