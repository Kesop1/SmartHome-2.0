package com.piotrak.service.controller.element;

import com.piotrak.service.controller.ElementController;
import com.piotrak.service.elementservice.SpeakersElementService;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.logger.WebLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.logging.Level;

/**
 * Controller for the speakers element
 */
@RestController
@RequestMapping("/speakers")
public class SpeakersController extends ElementController {

    @Autowired
    private WebLogger webLogger;

    private SpeakersElementService speakersElementService;

    public SpeakersController(@Autowired SpeakersElementService speakersElementService) {
        this.speakersElementService = speakersElementService;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    @Override
    protected ElementService getService() {
        return speakersElementService;
    }

    /**
     * Act on the switch command received from the GUI
     * @param cmd switch command
     * @return main page
     */
    @PostMapping
    public ModelAndView handleSwitchCommand(@RequestParam String cmd) {
        webLogger.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleCommand(getWebCommand(cmd));
    }
}


