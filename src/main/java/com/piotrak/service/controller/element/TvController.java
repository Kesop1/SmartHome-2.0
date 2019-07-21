package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.elementservice.TvElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the tv element
 */
@RestController
@RequestMapping("/tv")
public class TvController extends SwitchController {

    private Logger LOGGER = Logger.getLogger("TvController");

    private TvElementService tvElementService;

    public TvController(@Autowired TvElementService tvElementService) {
        this.tvElementService = tvElementService;
    }

    @Override
    protected ElementService getService() {
        return tvElementService;
    }

    /**
     * Act on the switch command received from the GUI
     * @param cmd switch command
     * @return main page
     */
    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleSwitchRequest(cmd);
    }

}


