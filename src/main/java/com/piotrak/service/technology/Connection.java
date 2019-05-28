package com.piotrak.service.technology;

import java.util.LinkedList;
import java.util.Queue;

public abstract class Connection {

    private Queue<Command> commandQueue = new LinkedList<>();
    
    public abstract void connect() throws ConnectionException;

    public abstract boolean isConnected();
    
    public abstract void disconnect();
    
    public abstract void send(Command command) throws ConnectionException;
    
    public abstract Command receive() throws ConnectionException;

    public Queue<Command> getCommandQueue() {
        return commandQueue;
    }
}
