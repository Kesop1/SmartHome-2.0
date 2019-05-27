package com.piotrak.service.controller;

import org.springframework.web.servlet.ModelAndView;

import javax.naming.OperationNotSupportedException;

public abstract class SwitchController extends AbstractController {

    protected ModelAndView handleSwitchRequest(String cmd){
        ModelAndView model = super.getModelAndView();
        try {
            String commandValue = getCommandValue(cmd);
            getService().commandReceived(getCommand(commandValue));
            model.setViewName("main");
        } catch (OperationNotSupportedException e) {
            model.setViewName(e.getMessage());//TODO: wyswietl error
        }
        return model;
    }

    private String getCommandValue(String cmd) throws OperationNotSupportedException {
        if(!"ON".equalsIgnoreCase(cmd) && !"OFF".equalsIgnoreCase(cmd)){
            throw new OperationNotSupportedException("Invalid command: '" + cmd + "' sent for the SwitchElement");
        }
        return cmd.toUpperCase();
    }
}
