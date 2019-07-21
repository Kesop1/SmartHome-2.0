package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the main view
 */
@RestController
public class MainController extends AbstractController {

    /**
     * Display the main page
     * @return main page
     */
    @RequestMapping({"/", "/home", "/main"})
    public ModelAndView mainGet() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("mainView");
        return model;
    }

}
