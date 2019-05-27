package com.piotrak.service.service;

import com.piotrak.service.element.Element;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import com.piotrak.service.technology.ConnectionService;
import com.piotrak.service.technology.gui.GuiCommand;

public abstract class ElementService implements Communication {

    private Element element;

    private ConnectionService connectionService;

    public ElementService(Element element, ConnectionService connectionService) {
        this.element = element;
        this.connectionService = connectionService;
    }

    public Element getElement(){
        return element;
    }

    public ConnectionService getConnectionService() {
        return connectionService;
    }

    public void commandReceived(Command command){
        actOnElement(command);
        if(command instanceof GuiCommand) {
            actOnConnection(command);
        }
    }

    protected void actOnElement(Command command){
        getElement().actOnCommand(command);
    }

    protected abstract void actOnConnection(Command command);

}
