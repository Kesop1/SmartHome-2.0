package com.piotrak.service.service;

import com.piotrak.service.element.Element;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import com.piotrak.service.technology.ConnectionService;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

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
        if(command instanceof WebCommand) {
            actOnConnection(command);
        }
    }

    protected void actOnElement(Command command){
        getElement().actOnCommand(command);
    }

    protected abstract void actOnConnection(Command command);

    @Scheduled(fixedDelay = 1000)
    @Async
    protected void checkCommandFromConnection(){
        Command command;
        do {
            command = getConnectionService().checkForCommand();
            if(command != null) {
                commandReceived(command);
            }
        }
        while(command != null);
    }

}
