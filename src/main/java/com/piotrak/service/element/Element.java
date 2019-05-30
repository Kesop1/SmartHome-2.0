package com.piotrak.service.element;

import com.piotrak.service.technology.Command;
import org.springframework.util.StringUtils;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;

public abstract class Element {

    private final String name;

    private final String displayName;

    public Element(@NotBlank String name) {
        this(name, name);
    }

    public Element(@NotBlank String name, String displayName) {
        assert !StringUtils.containsWhitespace(name);
        this.name = name;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public abstract void actOnCommand(Command command) throws OperationNotSupportedException;
}
