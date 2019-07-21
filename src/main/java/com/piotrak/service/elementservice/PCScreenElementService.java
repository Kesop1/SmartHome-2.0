package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * PC screen service for communication between systems
 */
@Service("pcScreenElementService")
@ConfigurationProperties("pc-screen")
public class PCScreenElementService extends ElementService implements MQTTCommunication {

    private Logger LOGGER = Logger.getLogger("PCScreenElementService");

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    public PCScreenElementService(@Autowired SwitchElement pcScreen, @Autowired MQTTConnectionService mqttConnectionService) {
        super(pcScreen, mqttConnectionService);
    }

    /**
     * Translate the command so it can be published in the element's publishTopic
     * @param command Command to be translated
     * @return MQTT command
     */
    @Override
    protected Command translateCommand(Command command) {
        return getMQTTPublishCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    @Override
    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    public void setSubscribeTopic(String subscribeTopic) {
        this.subscribeTopic = subscribeTopic;
    }

    @Override
    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }
}
