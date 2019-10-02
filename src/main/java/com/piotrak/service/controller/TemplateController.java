package com.piotrak.service.controller;

import com.piotrak.service.elementservice.TemplateElementService;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

/**
 * Controller for the templates
 */
@RestController
@RequestMapping("/template")
public class TemplateController extends AbstractController {

    @Autowired
    private WebLogger webLogger;

    private TemplateElementService templateElementService;

    public TemplateController(@Autowired TemplateElementService templateElementService) {
        this.templateElementService = templateElementService;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Controller for the template element request
     * @param name template to be activated
     * @return main page
     */
    @PostMapping
    public ModelAndView setTemplate(@RequestParam String name) {
        webLogger.log(Level.INFO, "Command received from web application: " + name);
        templateElementService.commandReceived(new WebCommand(name));
        return super.getModelAndView();
    }
}
