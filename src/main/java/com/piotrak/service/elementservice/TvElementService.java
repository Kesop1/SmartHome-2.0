package com.piotrak.service.elementservice;

import com.piotrak.service.DelayedCommandService;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ir.IRCommand;
import com.piotrak.service.technology.ir.IRCommunication;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * TV service for communication between systems
 */
@Service("tvElementService")
@ConfigurationProperties("tv")
public class TvElementService extends ElementService implements MQTTCommunication, IRCommunication {

    @Autowired
    private WebLogger webLogger;

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    private String irPublishTopic = "ir/default";

    private Map<String, String> irCode = new HashMap<>();

    private DelayedCommandService delayedCommandService;

    public TvElementService(@Autowired SwitchElement tv, @Autowired MQTTConnectionService mqttConnectionService,
                            @Autowired DelayedCommandService delayedCommandService) {
        super(tv, mqttConnectionService);
        this.delayedCommandService = delayedCommandService;
    }

    @Override
    protected MQTTCommand translateCommand(Command command) {
        return getMQTTPublishCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct//TODO
    @Override
    public void setUp() {
        webLogger.setUp(this.getClass().getName());
        super.setWebLogger(webLogger);
        webLogger.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    /**
     * Act on command, it can be an ON/OFF or a different IR command the device will respond to
     * @param command received command
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        webLogger.log(Level.INFO, "Command received:\t" + command);
        try {
            if (command instanceof IRCommand) {
                handleIrCommand((IRCommand) command);
            } else if (command instanceof MQTTCommand){
                handleMQTTCommand((MQTTCommand) command);
            } else {
                String cmd = command.getValue();
                if ("ON".equalsIgnoreCase(cmd) || "OFF".equalsIgnoreCase(cmd)) {
                    handleSwitchCommand(command);
                }
            }
        } catch (OperationNotSupportedException e){
            webLogger.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * ON or OFF command received, if WebCommand then send it also to the MQTT broker
     * @param command ON or OFF command
     * @throws OperationNotSupportedException when an incorrect message is received
     */
    private void handleSwitchCommand(Command command) throws OperationNotSupportedException {
        getElement().actOnCommand(command);
        if(command instanceof WebCommand) {
            if("ON".equalsIgnoreCase(command.getValue())) {
                handleOnCommand(command);
            } else {
                handleOffCommand(command);
            }
        }
    }

    /**
     * turn on the device, flip on the switch, also send the IR code
     * @param command ON command
     */
    private void handleOnCommand(Command command) {
        getConnectionService().actOnConnection(translateCommand(command));
        getDelayedCommandService().commandReceived(new DelayedCommand(1000, new IRCommand("on"), this));
    }

    /**
     * turn off the device, first send an IR code, and then flip off the switch
     * @param command OFF command
     */
    private void handleOffCommand(Command command) {
        try {
            handleIrCommand(new IRCommand("off"));
        } catch (OperationNotSupportedException e) {
            webLogger.log(Level.INFO, e.getMessage());
        }
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                getConnectionService().actOnConnection(translateCommand(command));
            } catch (InterruptedException e) {
                webLogger.log(Level.WARNING, e.getMessage());
            }
        }).start();
    }

    /**
     * Handle MQTT message from the broker
     * @param command MQTT command
     */
    private void handleMQTTCommand(MQTTCommand command) throws OperationNotSupportedException {
        webLogger.log(Level.INFO, "Sending command to the broker:\t" + command);
        getElement().actOnCommand(command);
    }

    /**
     * send out an IR command, command gets translated into an IR code
     * @param command IR command
     * @throws OperationNotSupportedException if no IR command was found in the IR codes map for the element
     */
    private void handleIrCommand(IRCommand command) throws OperationNotSupportedException{
        String irCodeForCommand = getIRCodeForCommand(command.getValue().toLowerCase());
        if (irCodeForCommand != null) {
            sendIRCommand(irCodeForCommand);
        } else{
            throw new OperationNotSupportedException("Unable to find an IR code for the command: " + command);
        }
    }

    /**
     * Send the IR code to the MQTT broker
     * @param irCode code to be sent
     */
    private void sendIRCommand(@NotBlank String irCode){
        getConnectionService().actOnConnection(new MQTTCommand(getIrPublishTopic(), irCode));
    }

    @Override
    public Map<String, String> getIRCodesMap() {
        return getIrCode();
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

    public DelayedCommandService getDelayedCommandService() {
        return delayedCommandService;
    }

    public void setDelayedCommandService(DelayedCommandService delayedCommandService) {
        this.delayedCommandService = delayedCommandService;
    }
}
