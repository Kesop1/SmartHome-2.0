package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import com.piotrak.service.service.ElementService;
import com.piotrak.service.technology.gui.GuiCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public abstract class AbstractController {

    protected abstract ElementService getService();

    @Autowired
    private Map<String, Map<String, Element>> elementsMap;

    ModelAndView getModelAndView(){
        ModelAndView model = new ModelAndView();
        model.addObject("elementsMap", elementsMap);
        return model;
    }

    GuiCommand getCommand(String cmd) {
        return new GuiCommand(cmd);
    }
}
