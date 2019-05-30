package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("pcScreenElementService")
public class PCScreenElementService extends ElementService implements MQTTCommunication {

    private Logger LOGGER = Logger.getLogger("PCScreenElementService");

    @Value("${mqtt.topic.subscribe.pcScreen}")
    private String subscribeTopic;

    @Value("${mqtt.topic.publish.pcScreen}")
    private String publishTopic;

    public PCScreenElementService(@Autowired SwitchElement pcScreen, @Autowired MQTTConnectionService mqttConnectionService) {
        super(pcScreen, mqttConnectionService);
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
    protected Command translateCommand(Command command) {
        return getMQTTPublishCommand(command);
    }

    @PostConstruct
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        //TODO: sciagnij status elementu - gdy w temacie .../status znajdzie sie wiadomość z arduino z flagą retained, przy subskrybcji do tematu zostanie ona od razu sciągnięta
        assert !StringUtils.isEmpty(getMQTTSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getMQTTSubscribeTopic(), this);
    }
}
