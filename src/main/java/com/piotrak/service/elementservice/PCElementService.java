package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;

/**
 * PC service for communication between systems
 */
@Service("pcElementService")
@ConfigurationProperties("pc")
public class PCElementService extends ElementService implements MQTTCommunication {

    @Autowired
    private WebLogger webLogger;

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    public PCElementService(@Autowired SwitchElement pc, @Autowired MQTTConnectionService mqttConnectionService) {
        super(pc, mqttConnectionService);
    }

    /**
     * Act on command
     * @param command Command received
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        try {
            String cmd = command.getValue();
            if(command instanceof MQTTCommand) {
                if ("ON".equalsIgnoreCase(cmd) || "OFF".equalsIgnoreCase(cmd)) {
                    getElement().actOnCommand(command);
                }
            } else if(command instanceof WebCommand) {
                if(command.getValue().equalsIgnoreCase("ON")){
                    if(((SwitchElement)getElement()).isOn()){
                        return;
                    }
                }
                getConnectionService().actOnConnection(translateCommand(command));
            }
        } catch (OperationNotSupportedException e) {
            webLogger.log(Level.WARNING, e.getMessage());
        }
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
}
