package com.piotrak.service.technology.ir;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotBlank;

/**
 * IR command
 */
public class IRCommand extends Command {

    public IRCommand(@NotBlank String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "IRCommand{" +
                "value='" + getValue() + '\'' +
                '}';
    }
}
