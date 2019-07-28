package com.piotrak.service.technology.time;

import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.Command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Command that will be sent after the delay time has passed
 */
public class DelayedCommand extends Command {

    /**
     * Time offset
     */
    private long delay;

    /**
     * Command that will be sent
     */
    private Command command;

    /**
     * ElementService that will receive the command
     */
    private ElementService elementService;

    public DelayedCommand(@Min(1) long delay, @NotNull Command command, @NotNull ElementService elementService) {
        this.delay = delay;
        this.command = command;
        this.elementService = elementService;
    }

    public long getDelay() {
        return delay;
    }

    public Command getCommand() {
        return command;
    }

    public ElementService getElementService() {
        return elementService;
    }

    @Override
    public String toString() {
        return "DelayedCommand{" +
                "delay='" + getDelay() + '\'' +
                "command='" + getCommand() + '\'' +
                "elementService='" + getElementService() + '\'' +
                '}';
    }
}
