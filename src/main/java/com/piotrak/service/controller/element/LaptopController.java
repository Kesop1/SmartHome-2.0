package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.LaptopElementService;
import com.piotrak.service.elementservice.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the laptop element
 */
@RestController
@RequestMapping("/laptop")
public class LaptopController extends SwitchController {

    private Logger LOGGER = Logger.getLogger("LaptopController");

    private LaptopElementService laptopElementService;

    public LaptopController(@Autowired LaptopElementService laptopElementService) {
        this.laptopElementService = laptopElementService;
    }

    @Override
    protected ElementService getService() {
        return laptopElementService;
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


