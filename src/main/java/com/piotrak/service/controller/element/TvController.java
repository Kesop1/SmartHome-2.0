package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.service.ElementService;
import com.piotrak.service.service.TvElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/tv")
public class TvController extends SwitchController {

    private TvElementService tvElementService;

    public TvController(@Autowired TvElementService tvElementService) {
        this.tvElementService = tvElementService;
    }

    @Override
    protected ElementService getService() {
        return tvElementService;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        return super.handleSwitchRequest(cmd);
    }

}


