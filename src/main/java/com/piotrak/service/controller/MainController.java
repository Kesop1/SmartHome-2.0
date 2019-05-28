package com.piotrak.service.controller;

import com.piotrak.service.service.ElementService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController extends AbstractController {

    @Override
    protected ElementService getService() {
        return null;
    }

    @GetMapping({"/", "/home", "/main"})
    public ModelAndView main() {
        //TODO: logger
        ModelAndView model = super.getModelAndView();
        model.setViewName("main");
        return model;
    }

}
