package com.piotrak.service.technology.time;

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
     * CommandService that will receive the command
     */
    private String element;

    public DelayedCommand(@Min(1) long delay, @NotNull Command command, @NotNull String element) {
        this.delay = delay;
        this.command = command;
        this.element = element;
    }

    public long getDelay() {
        return delay;
    }

    public Command getCommand() {
        return command;
    }

    public String getElement() {
        return element;
    }

    @Override
    public String toString() {
        return "DelayedCommand{" +
                "delay='" + getDelay() + '\'' +
                "command='" + getCommand() + '\'' +
                "element='" + getElement() + '\'' +
                '}';
    }
}
