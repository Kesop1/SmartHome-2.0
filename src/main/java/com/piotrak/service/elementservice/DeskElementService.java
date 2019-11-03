package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Desk service for communication between systems
 */
@Service("deskElementService")
@ConfigurationProperties("desk")
public class DeskElementService extends ElementService implements MQTTCommunication {//TODO: odpowiednia wysokość biurka

    @Autowired
    private WebLogger webLogger;

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    private String publishTopicHeight = "default";

    private Map<String, String> positionHeight = new HashMap<>();

    public DeskElementService(@Autowired SwitchElement desk, @Autowired MQTTConnectionService mqttConnectionService) {
        super(desk, mqttConnectionService);
    }

    /**
     * Translate the command so it can be published in the element's publishTopic
     * @param command Command to be translated
     * @return MQTT command
     */
    @Override
    protected Command translateCommand(Command command) {
        if(positionHeight.containsKey(command.getValue().toLowerCase())){
            return new MQTTCommand(getPublishTopicHeight(), positionHeight.get(command.getValue().toLowerCase()));
        } else {
            return getMQTTPublishCommand(command);
        }
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void setUp() {
        webLogger.setUp(this.getClass().getName());
        super.setWebLogger(webLogger);
        webLogger.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    public void setSubscribeTopic(String subscribeTopic) {
        this.subscribeTopic = subscribeTopic;
    }

    public String getPublishTopic() {
        return publishTopic;
    }

    public void setPublishTopic(String publishTopic) {
        this.publishTopic = publishTopic;
    }

    public Map<String, String> getPositionHeight() {
        return positionHeight;
    }

    public void setPositionHeight(Map<String, String> positionHeight) {
        this.positionHeight = positionHeight;
    }

    public String getPublishTopicHeight() {
        return publishTopicHeight;
    }

    public void setPublishTopicHeight(String publishTopicHeight) {
        this.publishTopicHeight = publishTopicHeight;
    }
}
