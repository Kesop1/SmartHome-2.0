package com.piotrak.service.controller;

import com.piotrak.service.logger.WebLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Controller for the calendar element
 */
@RestController
@RequestMapping("/calendar")
@ConfigurationProperties("calendar")
public class CalendarController extends AbstractController {

    private WebLogger webLogger;

    private Map<String, String> iFrame;

    @Autowired
    public CalendarController(WebLogger webLogger) {
        this.webLogger = webLogger;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Display the calendar page
     * @return calendar page
     */
    @GetMapping
    public ModelAndView geCalendar() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("calendarView");
        model.addObject("calendarsMap", iFrame);
        return model;
    }

    public Map<String, String> getiFrame() {
        return iFrame;
    }

    public void setiFrame(Map<String, String> iFrame) {
        this.iFrame = iFrame;
    }
}