package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.elementservice.VacuumElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the vacuum element
 */
@RestController
@RequestMapping("/vacuum")
public class VacuumController extends SwitchController {//TODO: można usunąć

    private Logger LOGGER = Logger.getLogger("VacuumController");

    private VacuumElementService vacuumElementService;

    public VacuumController(@Autowired VacuumElementService vacuumElementService) {
        this.vacuumElementService = vacuumElementService;
    }

    @Override
    protected ElementService getService() {
        return vacuumElementService;
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


