package com.piotrak.service.technology.time;

import com.piotrak.service.CommandService;
import com.piotrak.service.element.Element;
import com.piotrak.service.technology.Command;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.function.Predicate;

/**
 * Command that will be executed when a condition is met, if not 10 attempts will be made during the tryTime
 */
public class ConditionalCommand extends Command {

    /**
     * Was the command executed
     */
    private boolean ran = false;

    /**
     * Number of times the condition was checked
     */
    private int tryCount = 0;

    /**
     * Time, during which 10 condition checks will be made
     */
    private long tryTime;

    /**
     * Condition that needs to be met to run the command
     */
    private Predicate<Element> condition;

    /**
     * Command that will be sent
     */
    private Command command;

    /**
     * CommandService that will receive the command
     */
    private CommandService commandService;

    public ConditionalCommand(@NotNull Predicate<Element> condition, @NotNull Command command, @NotNull CommandService commandService, @Min(1) long tryTime) {
        this.condition = condition;
        this.command = command;
        this.commandService = commandService;
        this.tryTime = tryTime;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" +
                "condition='" + getCondition().toString() + '\'' +
                "command='" + getCommand() + '\'' +
                "commandService='" + getCommandService() + '\'' +
                "tryTime='" + getTryTime() + '\'' +
                '}';
    }

    public Predicate<Element> getCondition() {
        return condition;
    }

    public Command getCommand() {
        return command;
    }

    public CommandService getCommandService() {
        return commandService;
    }

    public boolean isRan() {
        return ran;
    }

    public int getTryCount() {
        return tryCount;
    }

    public void setRan(boolean ran) {
        this.ran = ran;
    }

    public void setTryCount(int tryCount) {
        this.tryCount = tryCount;
    }

    public long getTryTime() {
        return tryTime;
    }
}
