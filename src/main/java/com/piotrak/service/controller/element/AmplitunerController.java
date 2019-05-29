package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.AmplitunerElementService;
import com.piotrak.service.elementservice.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/amplituner")
public class AmplitunerController extends SwitchController {

    private Logger LOGGER = Logger.getLogger("AmplitunerController");

    private AmplitunerElementService amplitunerElementService;

    public AmplitunerController(@Autowired AmplitunerElementService amplitunerElementService) {
        this.amplitunerElementService = amplitunerElementService;
    }

    @Override
    protected ElementService getService() {
        return amplitunerElementService;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "AmplitunerController:\tCommand received from web application: " + cmd);
        return super.handleSwitchRequest(cmd);
    }
}


