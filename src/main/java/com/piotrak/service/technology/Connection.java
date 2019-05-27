package com.piotrak.service.technology;

public abstract class Connection {
    
    public abstract void connect() throws ConnectionException;
    
    public abstract void disconnect();
    
    public abstract void send(Command command) throws ConnectionException;
    
    public abstract Command receive() throws ConnectionException;
}
