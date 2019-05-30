package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.elementservice.PCElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/pc")
public class PCController extends SwitchController {

    private Logger LOGGER = Logger.getLogger("PCController");

    private PCElementService pcElementService;

    public PCController(@Autowired PCElementService pcElementService) {
        this.pcElementService = pcElementService;
    }

    @Override
    protected ElementService getService() {
        return pcElementService;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleSwitchRequest(cmd);
    }
}


