package com.piotrak.service.controller.element;

import com.piotrak.service.controller.ElementController;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.elementservice.PCScreenElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the pc screen element
 */
@RestController
@RequestMapping("/pcscreen")
public class PCScreenController extends ElementController {

    private Logger LOGGER = Logger.getLogger("PCScreenController");

    private PCScreenElementService pcScreenElementService;

    public PCScreenController(@Autowired PCScreenElementService pcScreenElementService) {
        this.pcScreenElementService = pcScreenElementService;
    }

    @Override
    protected ElementService getService() {
        return pcScreenElementService;
    }

    /**
     * Act on the switch command received from the GUI
     * @param cmd switch command
     * @return main page
     */
    @PostMapping
    public ModelAndView handleSwitchCommand(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleCommand(getWebCommand(cmd));
    }
}


