package com.piotrak.service;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotNull;

public abstract class CommandService {

    public abstract void commandReceived(@NotNull Command command);

}
