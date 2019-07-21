package com.piotrak.service.controller;

import com.piotrak.service.elementservice.TemplateElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the templates
 */
@RestController
@RequestMapping("/template")
public class TemplateController extends AbstractController {

    private Logger LOGGER = Logger.getLogger("TemplateController");

    private TemplateElementService templateElementService;

    public TemplateController(@Autowired TemplateElementService templateElementService) {
        this.templateElementService = templateElementService;
    }

    /**
     * Controller for the template element request
     * @param name template to be activated
     * @return main page
     */
    @PostMapping
    public ModelAndView setTemplate(@RequestParam String name) {
        LOGGER.log(Level.INFO, "Command received from web application: " + name);
        templateElementService.switchTemplate(name);
        ModelAndView model = super.getModelAndView();
        model.setViewName("mainView");
        return model;
    }
}
