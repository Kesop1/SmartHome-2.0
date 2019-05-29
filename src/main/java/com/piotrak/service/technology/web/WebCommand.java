package com.piotrak.service.technology.web;

import com.piotrak.service.technology.Command;

public class WebCommand extends Command {

    public WebCommand(String value) {
        super(value);
    }

    @Override
    public String toString() {
        return "WebCommand{" +
            "value='" + getValue() + '\'' +
            '}';
    }
}
