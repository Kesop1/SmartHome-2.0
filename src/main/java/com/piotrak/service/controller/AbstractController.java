package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

/**
 * Abstract controller for the webapp request
 */
public abstract class AbstractController {

    /**
     * All elements map
     */
    @Autowired
    private Map<String, List<Element>> elementsMap;

    /**
     * Get model and view for the webapp
     * @return model and view with elements
     */
    ModelAndView getModelAndView(){
        ModelAndView model = new ModelAndView();
        model.addObject("elementsMap", elementsMap);
        model.setViewName("redirect:/main");
        return model;
    }

    /**
     * Get command from the GUI
     * @param cmd user's command
     * @return WebCommand
     */
    protected WebCommand getWebCommand(String cmd) {
        return new WebCommand(cmd);
    }
}
