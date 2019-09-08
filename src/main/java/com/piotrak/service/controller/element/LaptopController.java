package com.piotrak.service.controller.element;

import com.piotrak.service.controller.ElementController;
import com.piotrak.service.elementservice.LaptopElementService;
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
 * Controller for the laptop element
 */
@RestController
@RequestMapping("/laptop")
public class LaptopController extends ElementController {

    @Autowired
    private WebLogger webLogger;

    private LaptopElementService laptopElementService;

    public LaptopController(@Autowired LaptopElementService laptopElementService) {
        this.laptopElementService = laptopElementService;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
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
    @PostMapping
    public ModelAndView handleSwitchCommand(@RequestParam String cmd) {//TODO: @RequestParam String switch
        webLogger.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleCommand(getWebCommand(cmd));
    }
}


