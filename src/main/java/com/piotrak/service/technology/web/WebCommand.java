package com.piotrak.service.technology.web;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotBlank;

/**
 * Command received from the GUI
 */
public class WebCommand extends Command {

    public WebCommand(@NotBlank String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "WebCommand{" +
            "value='" + getValue() + '\'' +
            '}';
    }
}
