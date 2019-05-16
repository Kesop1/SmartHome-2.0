package com.piotrak.service.technology;

public interface Connection {
    
    void connect() throws ConnectionException;
    
    void disconnect();
    
    void send(Command command) throws ConnectionException;
    
    Command receive() throws ConnectionException;
}
