package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {
    
    @GetMapping("/dashboard1")
    public ModelAndView dashboard1() {
        ModelAndView model = new ModelAndView();
        model.setViewName("dashboard1");
        return model;
    }

    @GetMapping("/dashboard2")
    public ModelAndView dashboard2() {
        ModelAndView model = new ModelAndView();
        model.setViewName("dashboard2");
        return model;
    }
}
