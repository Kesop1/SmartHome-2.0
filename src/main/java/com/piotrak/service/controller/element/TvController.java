package com.piotrak.service.controller.element;

import com.piotrak.service.controller.SwitchController;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/tv")
public class TvController extends SwitchController implements MQTTCommunication {
    
    private SwitchElement tv;
    
    @Value("${mqtt.topic.subscribe.tv}")
    private String subscribeTopic;
    
    @Value("${mqtt.topic.publish.tv}")
    private String publishTopic;

    public TvController(@Autowired SwitchElement tv) {
        this.tv = tv;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        return super.handleSwitchRequest(cmd);
    }

    @Override
    protected MQTTCommand getCommand(String cmd) {
        return createPublishCommand(cmd);//TODO: mapowanie "ON" na kod pilota
    }

    @Override
    protected SwitchElement getElement() {
        return tv;
    }

    @Override
    public String getPublishTopic() {
        return publishTopic;
    }
}


