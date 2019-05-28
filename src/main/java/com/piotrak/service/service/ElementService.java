package com.piotrak.service.service;

import com.piotrak.service.element.Element;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import com.piotrak.service.technology.ConnectionService;
import com.piotrak.service.technology.web.WebCommand;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;

public abstract class ElementService implements Communication {

    private Element element;

    private ConnectionService connectionService;

    public ElementService(@NotNull Element element, @NotNull ConnectionService connectionService) {
        this.element = element;
        this.connectionService = connectionService;
    }

    public Element getElement(){
        return element;
    }

    public ConnectionService getConnectionService() {
        return connectionService;
    }

    public void commandReceived(Command command) {
        try {
            getElement().actOnCommand(command);
            if(command instanceof WebCommand) {
                actOnConnection(command);
            }
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();//TODO
        }
    }

    protected abstract void actOnConnection(Command command);

}
