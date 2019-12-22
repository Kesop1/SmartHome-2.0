package com.piotrak.service.technology;

import com.piotrak.service.logger.WebLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

/**
 * Class for maintaining the connection
 */
public abstract class ConnectionService {

    @Autowired
    private WebLogger webLogger;

    /**
     * Connection
     */
    private Connection connection;

    public ConnectionService(Connection connection) {
        this.connection = connection;
        connect();
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
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
    @Scheduled(cron = "* * * * * ?")
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
            webLogger.log(Level.SEVERE, "Unable to connect to " + connection, e);
        }
    }

    /**
     * Send the command to the respective element service
     * @param command Command to be sent
     */
    public abstract void sendCommandToElementService(Command command);
}
