package com.piotrak.service.element;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;

public abstract class Element {
    
    private Connection connection;
    
    public Element(Connection connection) {
        this.connection = connection;
    }

    public void sendCommand(Command command) throws ConnectionException {
        getConnection().send(command);
    }

    public Command receiveCommand() throws ConnectionException {
        return getConnection().receive();
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
