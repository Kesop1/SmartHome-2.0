package com.piotrak.service.service;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
    public String getPublishTopic() {
        return publishTopic;
    }

    @Override
    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    @Override
    protected void actOnConnection(Command command) {
        getConnectionService().actOnCommand(getPublishCommand(command));//TODO: mapowanie "ON" na kod pilota
    }

    @Override
    public void subscribeToTopic(String topic) {
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic());//TODO
    }
}
