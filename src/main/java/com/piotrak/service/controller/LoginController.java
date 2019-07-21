package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the login requests
 */
@RestController
public class LoginController {

    /**
     * Display the login page
     * @return login page
     */
    @RequestMapping("/login")
    public ModelAndView getLoginPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("loginView");
        return model;
    }

    /**
     * Display the logout page
     * @return logout page
     */
    @RequestMapping("/logout")
    public ModelAndView logout() {
        ModelAndView model = new ModelAndView();
        model.setViewName("loginView");
        return model;
    }

}
