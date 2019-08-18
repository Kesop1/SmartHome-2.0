package com.piotrak.service;

import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.time.ConditionalCommand;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("conditionalCommandService")
public class ConditionalCommandService extends CommandService {

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    /**
     * Send the command if the condition is met, if not retry for 10 times
     * @param command Command to be checked and sent
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        LOGGER.log(Level.INFO, "Command received:\t" + command);
        if(!(command instanceof ConditionalCommand)){
            LOGGER.log(Level.SEVERE, "Incorrect command type passed to " + this.getClass().getName());
            return;
        }
        final ConditionalCommand conditionalCommand = (ConditionalCommand) command;
        while (!conditionalCommand.isRan() && conditionalCommand.getTryCount() < 10){
            if(conditionalCommand.getCondition().test(((ElementService)conditionalCommand.getCommandService()).getElement())){
                conditionalCommand.getCommandService().commandReceived(conditionalCommand.getCommand());
                conditionalCommand.setRan(true);
            } else {
                conditionalCommand.setTryCount(conditionalCommand.getTryCount() + 1);
                try {
                    Thread.sleep(conditionalCommand.getTryTime()/10);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.WARNING, e.getMessage());
                }
            }
        }
        conditionalCommand.setRan(false);
        conditionalCommand.setTryCount(0);
    }
}
