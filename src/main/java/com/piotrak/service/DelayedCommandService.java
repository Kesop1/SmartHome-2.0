package com.piotrak.service;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.time.DelayedCommand;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service("delayedCommandService")
public class DelayedCommandService extends CommandService {

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    /**
     * Send the command after delay time
     * @param command DelayedCommand to be sent
     */
    @Override
    public void commandReceived(Command command) {
        LOGGER.log(Level.INFO, "Command received:\t" + command);
        if(!(command instanceof DelayedCommand)){
            LOGGER.log(Level.SEVERE, "Incorrect command type passed to " + this.getClass().getName());
            return;
        }
        final DelayedCommand delayedCommand = (DelayedCommand) command;
        new Thread(() -> {
            try {
                Thread.sleep(delayedCommand.getDelay());
                delayedCommand.getCommandService().commandReceived(delayedCommand.getCommand());
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }).start();
    }
}
