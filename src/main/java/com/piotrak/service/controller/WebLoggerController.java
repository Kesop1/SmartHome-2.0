package com.piotrak.service.controller;

import com.piotrak.service.logger.WebLoggerQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Level;

@RestController
public class WebLoggerController {

    @Autowired
    private WebLoggerQueue webLoggerQueue;

    @GetMapping("/weblogger")
    public ModelAndView getLoggerPage() {
        ModelAndView model = new ModelAndView();
        model.setViewName("webLoggerView");
        model.addObject("logs", webLoggerQueue.getLogs());
        model.addObject("loglevel", webLoggerQueue.getLevel());
        model.addObject("logsize", webLoggerQueue.getSize());
        return model;
    }

    @PostMapping("/weblogger")
    public ModelAndView setLoggerOptions(@RequestParam String level, @RequestParam String size) {
        webLoggerQueue.setLevel(Level.parse(level.replaceAll(",", "")));
        webLoggerQueue.setSize(Integer.valueOf(size.replaceAll(",", "")));
        return getLoggerPage();
    }
}
