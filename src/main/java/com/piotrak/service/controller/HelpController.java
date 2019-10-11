package com.piotrak.service.controller;

import com.piotrak.service.element.Element;
import com.piotrak.service.logger.WebLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * Controller for the help element
 */
@RestController
@RequestMapping("/help")
public class HelpController extends AbstractController {

    private WebLogger webLogger;

    @Autowired
    public HelpController(WebLogger webLogger) {
        this.webLogger = webLogger;
    }

    @Autowired
    public Map<String, List<Element>> elementsMap;

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Display the help page
     * @return help page
     */
    @GetMapping
    public ModelAndView getHelp() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("helpView");
        model.addObject("elementsMap", elementsMap);
        return model;
    }
}