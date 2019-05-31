package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.elementservice.PS3ElementService;
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
@RequestMapping("/ps3")
public class PS3Controller extends SwitchController {

    private Logger LOGGER = Logger.getLogger("PS3Controller");

    private PS3ElementService ps3ElementService;

    public PS3Controller(@Autowired PS3ElementService ps3ElementService) {
        this.ps3ElementService = ps3ElementService;
    }

    @Override
    protected ElementService getService() {
        return ps3ElementService;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        LOGGER.log(Level.INFO, "Command received from web application: " + cmd);
        return super.handleSwitchRequest(cmd);
    }
}

