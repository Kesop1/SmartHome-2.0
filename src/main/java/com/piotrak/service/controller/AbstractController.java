package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

public abstract class AbstractController {

    protected abstract ElementService getService();

    @Autowired
    private Map<String, List<Element>> elementsMap;

    ModelAndView getModelAndView(){
        ModelAndView model = new ModelAndView();
        model.addObject("elementsMap", elementsMap);
        return model;
    }

    WebCommand getCommand(String cmd) {
        return new WebCommand(cmd);
    }
}
