package com.piotrak.service;

import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.time.DelayedCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

@Service("delayedCommandService")
public class DelayedCommandService extends CommandService {

    @Autowired
    private WebLogger webLogger;

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Send the command after delay time
     * @param command DelayedCommand to be sent
     */
    @Override
    public void commandReceived(Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        if(!(command instanceof DelayedCommand)){
            webLogger.log(Level.SEVERE, "Incorrect command type passed to " + this.getClass().getName());
            return;
        }
        final DelayedCommand delayedCommand = (DelayedCommand) command;
        new Thread(() -> {
            try {
                Thread.sleep(delayedCommand.getDelay());
                delayedCommand.getCommandService().commandReceived(delayedCommand.getCommand());
            } catch (InterruptedException e) {
                webLogger.log(Level.WARNING, e.getMessage());
            }
        }).start();
    }
}
