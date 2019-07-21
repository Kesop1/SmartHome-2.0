package com.piotrak.service.controller;

import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the SwitchElements
 */
public abstract class SwitchController extends ElementController {//TODO: SwitchController chyba można usunąć

    protected ModelAndView handleSwitchRequest(String cmd){//TODO: handleCommand(Command cmd)
        ModelAndView model = super.getModelAndView();
        getService().commandReceived(getCommand(cmd));
        model.setViewName("main");
        return model;
    }

}
