package com.piotrak.service.technology;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for maintaining the connection
 */
public abstract class ConnectionService {

    private Logger LOGGER = Logger.getLogger("ConnectionService");

    /**
     * Connection
     */
    private Connection connection;

    public ConnectionService(Connection connection) {
        this.connection = connection;
        connect();
    }

    protected Connection getConnection(){
        return connection;
    }

    /**
     * Send command to the connection
     * @param command Command to be sent
     */
    public abstract void actOnConnection(Command command);

    /**
     * Scan connection command queue
     */
    public void checkForCommands(){
        Command command;
        do {
            command = getConnection().getCommandQueue().poll();
            if (command != null) {
                sendCommandToElementService(command);
            }
        } while (command != null);
    }

    /**
     * Connect to the connection
     */
    private void connect(){
        try {
            connection.connect();
        } catch (ConnectionException e) {
            LOGGER.log(Level.SEVERE, "Unable to connect to " + connection, e);
        }
    }

    /**
     * Send the command to the respective element service
     * @param command Command to be sent
     */
    public abstract void sendCommandToElementService(Command command);
}
