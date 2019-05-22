package com.piotrak.service.controller;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.OperationNotSupportedException;

abstract class SwitchController extends AbstractController {

    abstract Command getCommand(String cmd);

    abstract SwitchElement getElement();

    ModelAndView handleSwitchRequest(String cmd){
        ModelAndView model = super.getModelAndView();
        try {
            String commandValue = getCommandValue(cmd);
            sendCommand(commandValue);
            switchElement(commandValue);
            model.setViewName("main");
        } catch (OperationNotSupportedException | ConnectionException e) {
            model.setViewName(e.getMessage());
        }
        return model;
    }

    private void sendCommand(String cmd) throws ConnectionException {
        Command command = getCommand(cmd);
        getElement().sendCommand(command);
    }

    private void switchElement(String cmd){
        getElement().setOn("ON".equals(cmd));
    }

    private String getCommandValue(String cmd) throws OperationNotSupportedException {
        if(!"ON".equalsIgnoreCase(cmd) && !"OFF".equalsIgnoreCase(cmd)){
            throw new OperationNotSupportedException("Invalid command: '" + cmd + "' sent for the SwitchElement");
        }
        return cmd.toUpperCase();
    }
}
