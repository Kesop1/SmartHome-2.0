package com.piotrak.service.technology;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Connection with a service or technology
 */
public abstract class Connection {

    /**
     * Commands received using the connection that are to be dealt with
     */
    private Queue<Command> commandQueue = new LinkedList<>();

    /**
     * Connect to the service
     * @throws ConnectionException when unable to connect
     */
    public abstract void connect() throws ConnectionException;

    /**
     * Is the application connected
     * @return true if the application is connected
     */
    public abstract boolean isConnected();

    /**
     * Disconnect from the service or technology
     */
    public abstract void disconnect();

    /**
     * Send a message using service
     * @param command Command to be send
     * @throws ConnectionException when an error occurs during sending or an incorrect message is received
     */
    public abstract void send(Command command) throws ConnectionException;

    /**
     * Prepare the connection for receiving commands
     * @return Command received
     * @throws ConnectionException when an error occurs during receiveing
     */
    public abstract Command receive() throws ConnectionException;

    public Queue<Command> getCommandQueue() {
        return commandQueue;
    }
}
