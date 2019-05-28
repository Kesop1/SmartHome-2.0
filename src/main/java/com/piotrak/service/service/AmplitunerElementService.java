package com.piotrak.service.service;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service("amplitunerElementService")
public class AmplitunerElementService extends ElementService implements MQTTCommunication {

    @Value("${mqtt.topic.subscribe.amplituner}")
    private String subscribeTopic;

    @Value("${mqtt.topic.publish.amplituner}")
    private String publishTopic;

    public AmplitunerElementService(@Autowired SwitchElement amplituner, @Autowired MQTTConnectionService mqttConnectionService) {
        super(amplituner, mqttConnectionService);
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

    @PostConstruct
    @Override
    public void subscribeToMQTTTopic() {
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getMQTTSubscribeTopic(), getElement());//TODO
    }
}
