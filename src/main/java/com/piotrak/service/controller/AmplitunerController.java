package com.piotrak.service.controller;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/amplituner")
public class AmplitunerController extends SwitchController implements MQTTCommunication {

    private SwitchElement amplituner;

    @Value("${mqtt.topic.subscribe.amplituner}")
    private String subscribeTopic;

    @Value("${mqtt.topic.publish.amplituner}")
    private String publishTopic;

    public AmplitunerController(@Autowired SwitchElement amplituner) {
        this.amplituner = amplituner;
    }

    @Override
    @PostMapping
    public ModelAndView handleSwitchRequest(@RequestParam String cmd) {
        return super.handleSwitchRequest(cmd);
    }

    @Override
    MQTTCommand getCommand(String cmd) {
        return createPublishCommand(cmd);//TODO: mapowanie "ON" na kod pilota
    }

    @Override
    SwitchElement getElement() {
        return amplituner;
    }

    @Override
    public String getPublishTopic() {
        return publishTopic;
    }
}


