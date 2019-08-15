package com.piotrak.service.element;

import com.piotrak.service.CommandService;
import com.piotrak.service.action.Switchable;
import com.piotrak.service.technology.Command;
import org.apache.commons.collections4.MultiValuedMap;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * Template element that can be used to group functions to be performed together
 */
public class TemplateElement extends Element implements Switchable {

    private boolean active = false;

    /**
     * Commands to be performed when template is activated
     */
    private MultiValuedMap<CommandService, Command> elementCommandMap;

    public TemplateElement(@NotBlank String name, String displayName, MultiValuedMap<CommandService, Command> elementCommandMap) {
        super(name, displayName);
        this.elementCommandMap = elementCommandMap;
    }

    /**
     * Perform action on command received from the GUI
     * @param command Command received
     * @throws OperationNotSupportedException when an incorrect command is received
     */
    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        switchElement(command.getValue());
    }

    /**
     * Is the template active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set the template active or not
     * @param active element's active state
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    public MultiValuedMap<CommandService, Command> getElementCommandMap() {
        return elementCommandMap;
    }

    /**
     * Perform all the actions for the template if command is "ON", if it's "OFF" disactivate the template
     * @param command ON or OFF
     * @throws OperationNotSupportedException when an incorrect command is received
     */
    @Override
    public void switchElement(String command) throws OperationNotSupportedException {
        if ("ON".equalsIgnoreCase(command) || "OFF".equalsIgnoreCase(command)) {
            setActive("ON".equalsIgnoreCase(command));
        } else {
            throw new OperationNotSupportedException("Invalid command: '" + command + "' sent for : " + getName());
        }
        if(isActive()){
            for (Map.Entry entry : getElementCommandMap().entries()){
                CommandService service = (CommandService) entry.getKey();
                service.commandReceived((Command) entry.getValue());
            }
        }
    }
}
