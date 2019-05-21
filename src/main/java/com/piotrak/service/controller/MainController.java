package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
public class MainController {

    @Autowired
    private Map<String, Map<String, Element>> elementsMap;
    
    @GetMapping({"/", "/home", "/main"})
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        model.setViewName("main");
        model.addObject("elementsMap", elementsMap);
        return model;
    }

}
