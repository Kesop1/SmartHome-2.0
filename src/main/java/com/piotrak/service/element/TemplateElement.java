package com.piotrak.service.element;

import com.piotrak.service.action.Switchable;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import java.util.Map;

public class TemplateElement extends Element implements Switchable {

    private boolean active = false;

    private Map<ElementService, Command> elementCommandMap;

    public TemplateElement(@NotBlank String name, String displayName, Map<ElementService, Command> elementCommandMap) {
        super(name, displayName);
        this.elementCommandMap = elementCommandMap;
    }

    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        switchElement(command.getValue());
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Map<ElementService, Command> getElementCommandMap() {
        return elementCommandMap;
    }

    @Override
    public void switchElement(String command) throws OperationNotSupportedException {
        if ("ON".equalsIgnoreCase(command) || "OFF".equalsIgnoreCase(command)) {
            setActive("ON".equalsIgnoreCase(command));
        } else {
            throw new OperationNotSupportedException("Invalid command: '" + command + "' sent for : " + getName());
        }
        if(isActive()){
            for (Map.Entry entry : getElementCommandMap().entrySet()){
                ElementService service = (ElementService) entry.getKey();
                service.commandReceived((Command) entry.getValue());
            }
        }
    }
}
