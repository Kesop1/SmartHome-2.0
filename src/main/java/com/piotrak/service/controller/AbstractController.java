package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

public abstract class AbstractController {

    @Autowired
    private Map<String, Map<String, Element>> elementsMap;

    ModelAndView getModelAndView(){
        ModelAndView model = new ModelAndView();
        model.addObject("elementsMap", elementsMap);
        return model;
    }
}
