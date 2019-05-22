package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController extends AbstractController {
    
    @GetMapping({"/", "/home", "/main"})
    public ModelAndView main() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("main");
        return model;
    }

}
