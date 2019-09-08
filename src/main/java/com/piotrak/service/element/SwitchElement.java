package com.piotrak.service.element;

import com.piotrak.service.action.Inactivable;
import com.piotrak.service.action.Switchable;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import java.util.logging.Level;

/**
 * Element that can be switched
 */
public class SwitchElement extends Element implements Switchable, Inactivable {

    @Autowired
    private WebLogger webLogger;

    /**
     * Element's switch state
     */
    private boolean on = false;

    /**
     * Element's active state
     */
    private boolean active = true;

    public SwitchElement(@NotBlank String name) {
        super(name);
    }

    public SwitchElement(@NotBlank String name, String displayName) {
        super(name, displayName);
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Is the element ON?
     * @return element's switch state
     */
    public boolean isOn() {
        return on;
    }

    /**
     * Set the device state
     * @param on element's switch state
     */
    public void setOn(boolean on) {
        webLogger.log(Level.INFO, getName() + " has been switched " + (on ? "on" : "off"));
        this.on = on;
    }

    /**
     * Switch the element based on the command
     * @param command Command received
     * @throws OperationNotSupportedException when an incorrect switch command is received
     */
    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        switchElement(command.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void switchElement(String command) throws OperationNotSupportedException {
        if ("ON".equalsIgnoreCase(command) || "OFF".equalsIgnoreCase(command)) {
            setOn("ON".equalsIgnoreCase(command));
        } else {
            throw new OperationNotSupportedException("Invalid command: '" + command + "' sent for : " + getName());
        }
    }

    /**
     * Is the element active?
     * @return element's active state
     */
    @Override
    public boolean isActive() {
        return active;
    }

    /**
     * Set the device active state
     * @param active element's active state
     */
    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
