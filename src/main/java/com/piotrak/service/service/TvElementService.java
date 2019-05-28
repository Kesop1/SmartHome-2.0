package com.piotrak.service.service;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("tvElementService")
public class TvElementService extends ElementService implements MQTTCommunication {

    @Value("${mqtt.topic.subscribe.tv}")
    private String subscribeTopic;

    @Value("${mqtt.topic.publish.tv}")
    private String publishTopic;

    public TvElementService(@Autowired SwitchElement tv, @Autowired MQTTConnectionService mqttConnectionService) {
        super(tv, mqttConnectionService);
    }

    @Override
    public String getMQTTPublishTopic() {
        return publishTopic;
    }

    @Override
    public String getMQTTSubscribeTopic() {
        return subscribeTopic;
    }

    @Override
    protected void actOnConnection(Command command) {
        getConnectionService().actOnCommand(getMQTTPublishCommand(command));//TODO: mapowanie "ON" na kod pilota
    }

    @PostConstruct//TODO
    @Override
    public void subscribeToMQTTTopic() {
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getMQTTSubscribeTopic(), getElement());//TODO
    }
}
