package com.piotrak.service.elementservice;

import com.piotrak.service.DelayedCommandService;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import com.piotrak.service.technology.time.DelayedCommand;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Desk service for communication between systems
 */
@Service("deskElementService")
@ConfigurationProperties("desk")
public class DeskElementService extends ElementService implements MQTTCommunication {

    @Autowired
    private WebLogger webLogger;

    private String subscribeTopic = "default/status";

    private String subscribeTopicHeight = "default/status";

    private String publishTopic = "default";

    private String publishTopicHeight = "default";

    private Map<String, String> positionHeight = new HashMap<>();

    private DelayedCommandService delayedCommandService;

    public DeskElementService(@Autowired SwitchElement desk, @Autowired MQTTConnectionService mqttConnectionService,
                              DelayedCommandService delayedCommandService) {
        super(desk, mqttConnectionService);
        this.delayedCommandService = delayedCommandService;
    }

    /**
     * Act on command, it can be an ON/OFF or a desk height setup name
     * in case a desk height change is requested first turn Desk ON then change height
     * if a desk height was set and message published by the device, turn it OFF
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        try {
            if(command instanceof WebCommand) {
                if(positionHeight.containsKey(command.getValue().toLowerCase()) || command.getValue().matches("-?\\d+(\\.\\d+)?")){
                    getConnectionService().actOnConnection(new MQTTCommand(getPublishTopic(),"ON"));
                }
                getConnectionService().actOnConnection(translateCommand(command));
            } else {
                if (command.getValue().matches("-?\\d+(\\.\\d+)?")) {
                    getConnectionService().actOnConnection(new MQTTCommand(getPublishTopic(), "OFF"));
                } else {
                    getElement().actOnCommand(command);
                }
            }
        } catch (OperationNotSupportedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Translate the command so it can be published in the element's publishTopic
     * @param command Command to be translated
     * @return MQTT command
     */
    @Override
    protected Command translateCommand(Command command) {
        if(positionHeight.containsKey(command.getValue().toLowerCase())) {
            return new MQTTCommand(getPublishTopicHeight(), positionHeight.get(command.getValue().toLowerCase()));
        } else if(command.getValue().matches("-?\\d+(\\.\\d+)?")){
            return new MQTTCommand(getPublishTopicHeight(), command.getValue());
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
        assert !StringUtils.isEmpty(getSubscribeTopicHeight());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopicHeight(), this);
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

    public DelayedCommandService getDelayedCommandService() {
        return delayedCommandService;
    }

    public void setDelayedCommandService(DelayedCommandService delayedCommandService) {
        this.delayedCommandService = delayedCommandService;
    }

    public String getSubscribeTopicHeight() {
        return subscribeTopicHeight;
    }

    public void setSubscribeTopicHeight(String subscribeTopicHeight) {
        this.subscribeTopicHeight = subscribeTopicHeight;
    }
}
