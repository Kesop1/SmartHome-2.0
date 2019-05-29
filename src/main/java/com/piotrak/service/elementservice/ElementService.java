package com.piotrak.service.elementservice;

import com.piotrak.service.element.Element;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import com.piotrak.service.technology.ConnectionService;
import com.piotrak.service.technology.web.WebCommand;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ElementService implements Communication {

    private Logger LOGGER = Logger.getLogger("ElementService");

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
        assert command != null;
        LOGGER.log(Level.INFO, "Command received:\t" + command);
        try {
            getElement().actOnCommand(command);
            if(command instanceof WebCommand) {
                actOnConnection(command);
            }
        } catch (OperationNotSupportedException e) {
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    protected abstract void actOnConnection(Command command);

}
