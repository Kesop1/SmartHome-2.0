package com.piotrak.service.element;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;

public abstract class Element {
    
    private Connection connection;
    
    public Element(Connection connection) {
        this.connection = connection;
    }
    
    public abstract void sendCommand(Command command);
    
    public abstract Command receiveCommand();
    
    public Connection getConnection() {
        return connection;
    }
    
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
