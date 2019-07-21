package com.piotrak.service.element;

import com.piotrak.service.technology.Command;
import org.springframework.util.StringUtils;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;

/**
 * Basic element representing a device
 */
public abstract class Element {

    /**
     * Name of the element
     */
    private final String name;

    /**
     * Display name of the element
     */
    private final String displayName;

    public Element(@NotBlank String name) {
        this(name, name);
    }

    public Element(@NotBlank String name, String displayName) {
        assert !StringUtils.containsWhitespace(name);
        this.name = name;
        this.displayName = displayName;
    }

    /**
     * Get the element's name
     * @return element's name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the element's display name
     * @return element's display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Do something with the received command
     * @param command Command received
     * @throws OperationNotSupportedException when an incorrect command is received for the element
     */
    public abstract void actOnCommand(Command command) throws OperationNotSupportedException;
}
