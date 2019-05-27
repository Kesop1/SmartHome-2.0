package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.service.AmplitunerElementService;
import com.piotrak.service.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/amplituner")
public class AmplitunerController extends SwitchController {

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
        return super.handleSwitchRequest(cmd);
    }
}


