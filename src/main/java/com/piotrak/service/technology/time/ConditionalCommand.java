package com.piotrak.service.technology.time;

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
    private Predicate<String> condition;

    /**
     * Command that will be sent
     */
    private Command command;

    /**
     * CommandService that will receive the command
     */
    private String element;

    public ConditionalCommand(@NotNull Predicate<String> condition, @NotNull Command command, @NotNull String element, @Min(1) long tryTime) {
        this.condition = condition;
        this.command = command;
        this.element = element;
        this.tryTime = tryTime;
    }

    @Override
    public String toString() {
        return this.getClass().getName() + "{" +
                "condition='" + getCondition().toString() + '\'' +
                "command='" + getCommand() + '\'' +
                "element='" + getElement() + '\'' +
                "tryTime='" + getTryTime() + '\'' +
                '}';
    }

    public Predicate<String> getCondition() {
        return condition;
    }

    public Command getCommand() {
        return command;
    }

    public String getElement() {
        return element;
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
