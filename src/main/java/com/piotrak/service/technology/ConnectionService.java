package com.piotrak.service.technology;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ConnectionService {

    private Logger LOGGER = Logger.getLogger("ConnectionService");

    private Connection connection;

    public ConnectionService(Connection connection) {
        this.connection = connection;
        connect();
    }

    protected Connection getConnection(){
        return connection;
    }

    public abstract void actOnConnection(Command command);

    public void checkForCommands(){
        Command command;
        do {
            command = getConnection().getCommandQueue().poll();
            if (command != null) {
                sendCommandToElementService(command);
            }
        } while (command != null);
    }

    private void connect(){
        try {
            connection.connect();
        } catch (ConnectionException e) {
            LOGGER.log(Level.SEVERE, "Unable to connect to " + connection, e);
        }
    }

    public abstract void sendCommandToElementService(Command command);
}
