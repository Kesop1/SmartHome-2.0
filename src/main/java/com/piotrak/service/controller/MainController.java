package com.piotrak.service.controller;

import com.piotrak.service.elementservice.ElementService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class MainController extends AbstractController {

    @Override
    protected ElementService getService() {
        return null;
    }

    @RequestMapping({"/", "/home", "/main"})
    public ModelAndView mainGet() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("mainView");
        return model;
    }

}
