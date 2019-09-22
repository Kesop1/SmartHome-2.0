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
     * Display the main page, sleep for 1,5 sec to allow device status from MQTT to update
     * @return main page
     */
    @RequestMapping({"/", "/home", "/main"})
    public ModelAndView mainGet() {
        ModelAndView model = null;
        try {
            model = super.getModelAndView();
            model.setViewName("mainView");
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return model;
    }

}
