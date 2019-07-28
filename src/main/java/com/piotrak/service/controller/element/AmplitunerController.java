package com.piotrak.service.controller.element;

import com.piotrak.service.controller.ElementController;
import com.piotrak.service.elementservice.AmplitunerElementService;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.ir.IRCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the amplituner element
 */
@RestController
@RequestMapping("/amplituner")
public class AmplitunerController extends ElementController {

    private Logger LOGGER = Logger.getLogger("AmplitunerController");

    private AmplitunerElementService amplitunerElementService;

    public AmplitunerController(@Autowired AmplitunerElementService amplitunerElementService) {
        this.amplitunerElementService = amplitunerElementService;
    }

    @Override
    protected ElementService getService() {
        return amplitunerElementService;
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

    /**
     * Act on the IR command received from the GUI
     * @param cmd ir command
     * @return main page
     */
    @PostMapping("/ir")
    public ModelAndView handleIRCommand(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "IRCommand received from web application: " + cmd);
        return super.handleCommand(new IRCommand(cmd));
    }
}