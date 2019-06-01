package com.piotrak.service.controller;

import org.springframework.web.servlet.ModelAndView;

public abstract class SwitchController extends ElementController {

    protected ModelAndView handleSwitchRequest(String cmd){
        ModelAndView model = super.getModelAndView();
        getService().commandReceived(getCommand(cmd));
        model.setViewName("main");
        return model;
    }

}
