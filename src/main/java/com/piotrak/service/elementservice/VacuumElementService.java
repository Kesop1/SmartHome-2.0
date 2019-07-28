package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ir.IRCommand;
import com.piotrak.service.technology.ir.IRCommunication;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
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
import java.util.logging.Logger;

/**
 * Vacuum service for communication between systems
 */
@Service("vacuumElementService")
@ConfigurationProperties("vacuum")
public class VacuumElementService extends ElementService implements MQTTCommunication, IRCommunication {

    private Logger LOGGER = Logger.getLogger("VacuumElementService");

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    private String irPublishTopic = "ir/default";

    private Map<String, String> irCode = new HashMap<>();

    public VacuumElementService(@Autowired SwitchElement vacuum, @Autowired MQTTConnectionService mqttConnectionService) {//TODO: SwitchElement vacuum można usunąć, tylko IR commands są używane
        super(vacuum, mqttConnectionService);
    }

    /**
     * Translate the command so it can be published in the element's publishTopic
     * @param command Command to be translated
     * @return MQTT command
     */
    @Override
    protected Command translateCommand(Command command) {//TODO: oodkurzacz nie publikuje wiadomości
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

    /**
     * Act on command, it can only be an IR command the device will respond to
     * @param command received command
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        LOGGER.log(Level.INFO, "Command received:\t" + command);
        try {
            if (command instanceof IRCommand){
                handleIrCommand((IRCommand) command);
            } else {
                throw new OperationNotSupportedException("Command not recognized: " + command);
            }
        } catch (OperationNotSupportedException e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * Send the IRCommand to the MQTT broker
     * @param command IR command
     */
    private void handleIrCommand(IRCommand command) {
        String irCode = getIRCodeForCommand(command.getValue().toLowerCase());
        getConnectionService().actOnConnection(new MQTTCommand(getIrPublishTopic(), irCode));
    }

    @Override
    public Map<String, String> getIRCodesMap() {
        return getIrCode();
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

    public Map<String, String> getIrCode() {
        return irCode;
    }

    public void setIrCode(Map<String, String> irCode) {
        this.irCode = irCode;
    }

    public String getIrPublishTopic() {
        return irPublishTopic;
    }

    public void setIrPublishTopic(String irPublishTopic) {
        this.irPublishTopic = irPublishTopic;
    }
}
