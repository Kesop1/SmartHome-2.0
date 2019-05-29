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

@Service("tvElementService")
public class TvElementService extends ElementService implements MQTTCommunication {//TODO: MQTTElementService

    private Logger LOGGER = Logger.getLogger("TvElementService");

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
        getConnectionService().actOnConnection(getMQTTPublishCommand(command));//TODO: mapowanie "ON" na kod pilota
    }

    @PostConstruct//TODO
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        //TODO: sciagnij status elementu
        assert !StringUtils.isEmpty(getMQTTSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getMQTTSubscribeTopic(), this);
    }
}
