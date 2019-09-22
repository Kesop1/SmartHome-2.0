package com.piotrak.service;

import com.piotrak.config.ServicesConfiguration;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.time.ScheduledCommand;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

@Service("scheduledCommandService")
public class ScheduledCommandService extends CommandService {

    @Autowired
    private WebLogger webLogger;

    @Autowired
    private ServicesConfiguration servicesConfiguration;

    private List<ScheduledCommand> scheduledCommands = new LinkedList<>();

    private static AtomicLong idCounter = new AtomicLong();

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Add a scheduled job to the queue
     * @param command ScheduledCommand to be added
     */
    @Override
    public void commandReceived(Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        if(!(command instanceof ScheduledCommand)){
            webLogger.log(Level.SEVERE, "Incorrect command type passed to " + this.getClass().getName());
            return;
        }
        final ScheduledCommand scheduledCommand = (ScheduledCommand) command;
        addToQueue(scheduledCommand);
    }

    private void addToQueue(ScheduledCommand scheduledCommand) {
        if(!scheduledCommands.isEmpty()){
            for(int i = 0; i<scheduledCommands.size(); i++){
                ScheduledCommand command = scheduledCommands.get(i);
                if(scheduledCommand.getDate().before(command.getDate())){
                    scheduledCommands.add(i, scheduledCommand);
                    return;
                }
            }
        }
        scheduledCommands.add(scheduledCommand);
        scheduledCommand.setId(createID());
    }

    public List<ScheduledCommand> getScheduledCommands() {
        return scheduledCommands;
    }

    /**
     * Check every minute if any scheduled job is due
     */
    @Scheduled(cron = "0 * * * * *")
    @Async
    private void runScheduledCommandJob(){
        webLogger.log(Level.FINE, "Running scheduledCommandJob");
        Date now = new Date(System.currentTimeMillis());
        ListIterator<ScheduledCommand> iterator = scheduledCommands.listIterator();
        while(iterator.hasNext()){
            ScheduledCommand command = iterator.next();
            if(command.getDate().before(now)){
                webLogger.log(Level.FINE, String.format("Executing scheduledCommandJob: %s", command));
                CommandService commandService = servicesConfiguration.getServiceMap().get(command.getElement());
                WebCommand webCommand = new WebCommand(command.getValue());
                commandService.commandReceived(webCommand);
                iterator.remove();
            } else {
                return;
            }
        }
    }

    /**
     * Remove a scheduled job from the queue
     * @param jobId
     */
    public void removeScheduledJob(String jobId) {
        Iterator<ScheduledCommand> iterator = scheduledCommands.iterator();
        while (iterator.hasNext()){
            ScheduledCommand command = iterator.next();
            if(command.getId().equals(jobId)){
                webLogger.log(Level.FINE, String.format("Removing scheduledCommandJob: %s", command));
                iterator.remove();
                return;
            }
        }
    }

    private static String createID() {
        return String.valueOf(idCounter.getAndIncrement());
    }
}
