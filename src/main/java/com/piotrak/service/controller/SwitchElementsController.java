package com.piotrak.service.controller;

import com.piotrak.service.element.SwitchElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwitchElementsController {
    
    @Autowired
    private SwitchElement tv;
    
    @GetMapping("/tv")
    public String switchTv(@RequestParam(value = "cmd", required = false, defaultValue = "OFF") String cmd) {
        return "TV is " + cmd;
    }
}


