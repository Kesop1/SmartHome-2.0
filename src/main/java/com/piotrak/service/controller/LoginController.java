package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("loginView");
        return model;
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView model = new ModelAndView();
        model.setViewName("loginView");
        return model;
    }

}