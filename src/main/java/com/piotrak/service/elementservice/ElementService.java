package com.piotrak.service.elementservice;

import com.piotrak.service.CommandService;
import com.piotrak.service.element.Element;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import com.piotrak.service.technology.ConnectionService;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;

/**
 * Service for communication between systems for the element
 */
public abstract class ElementService extends CommandService implements Communication {

    @Autowired
    private WebLogger webLogger;

    /**
     * Element of the service
     */
    private Element element;

    /**
     * Element's connection with the application
     */
    private ConnectionService connectionService;

    public ElementService(@NotNull Element element, @NotNull ConnectionService connectionService) {
        this.element = element;
        this.connectionService = connectionService;
    }

    public void setWebLogger(WebLogger webLogger) {
        this.webLogger = webLogger;
    }

    public Element getElement(){
        return element;
    }

    public ConnectionService getConnectionService() {
        return connectionService;
    }

    /**
     * Act on command
     * @param command Command received
     */
    @Override
    public void commandReceived(Command command) {//TODO notnull
        assert command != null;
        webLogger.log(Level.INFO, "Command received:\t" + command);
        try {
            getElement().actOnCommand(command);
            if(command instanceof WebCommand) {
                getConnectionService().actOnConnection(translateCommand(command));
            }
        } catch (OperationNotSupportedException e) {
            webLogger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Translate the command for the element communication
     * @param command Command to be translated
     * @return Translated command
     */
    protected abstract Command translateCommand(Command command);

}
