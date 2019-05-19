package com.piotrak.service.controller;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.naming.OperationNotSupportedException;

@RestController
@RequestMapping("/tv")
public class TvController extends SwitchController{
    
    @Autowired
    private SwitchElement tv;
    
    @Value("${mqtt.topic.subscribe.tv}")
    private String subscribeTopic;
    
    @Value("${mqtt.topic.publish.tv}")
    private String publishTopic;
    
    @GetMapping("/tv")
    public String switchTv(@RequestParam(value = "cmd", required = false, defaultValue = "OFF") String cmd) {
        try{
            String command = super.getCommand(cmd);
            MQTTCommand mqttCommand = new MQTTCommand(publishTopic, command);//TODO: mapowanie "ON" na kod pilota
            tv.sendCommand(mqttCommand);
            tv.setOn("ON".equals(command));
        }catch (OperationNotSupportedException onse){
            return onse.getMessage();
        }
        
        return "TV is " + (tv.isOn() ? "ON" : "OFF");//TODO: zwracaj html
    }
}


