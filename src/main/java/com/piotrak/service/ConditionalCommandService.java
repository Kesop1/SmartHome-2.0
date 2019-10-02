package com.piotrak.service;

import com.piotrak.config.ServicesConfiguration;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.time.ConditionalCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;

@Service("conditionalCommandService")
public class ConditionalCommandService extends CommandService {

    @Autowired
    private WebLogger webLogger;

    private ServicesConfiguration servicesConfiguration;

    @Autowired
    public ConditionalCommandService(ServicesConfiguration servicesConfiguration) {
        this.servicesConfiguration = servicesConfiguration;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Send the command if the condition is met, if not retry for 10 times
     * @param command Command to be checked and sent
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        if(!(command instanceof ConditionalCommand)){
            webLogger.log(Level.SEVERE, "Incorrect command type passed to " + this.getClass().getName());
            return;
        }
        final ConditionalCommand conditionalCommand = (ConditionalCommand) command;
        while (!conditionalCommand.isRan() && conditionalCommand.getTryCount() < 10){
            if(conditionalCommand.getCondition().test(conditionalCommand.getElement())){
                servicesConfiguration.getServiceMap().get(conditionalCommand.getElement()).commandReceived(conditionalCommand.getCommand());
                conditionalCommand.setRan(true);
            } else {
                conditionalCommand.setTryCount(conditionalCommand.getTryCount() + 1);
                try {
                    Thread.sleep(conditionalCommand.getTryTime()/10);
                } catch (InterruptedException e) {
                    webLogger.log(Level.WARNING, e.getMessage());
                }
            }
        }
        conditionalCommand.setRan(false);
        conditionalCommand.setTryCount(0);
    }
}
