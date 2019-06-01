package com.piotrak.service.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/schema")
public class SchemaController extends AbstractController {

    //TODO: SchemaService
    @PostMapping
    public ModelAndView setSchema(@RequestParam String name) {
        ModelAndView model = super.getModelAndView();
        model.setViewName("mainView");
        return model;
    }
}
