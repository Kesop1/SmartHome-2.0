package com.piotrak.service.elementservice;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ir.IRCommunication;
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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("amplitunerElementService")
@ConfigurationProperties("amplituner")
public class AmplitunerElementService extends ElementService implements MQTTCommunication, IRCommunication {

    private Logger LOGGER = Logger.getLogger("AmplitunerElementService");

    private String subscribeTopic = "default/status";

    private String publishTopic = "default";

    private String irPublishTopic = "ir/default";

    private Map<String, String> irCode = new HashMap<>();

    public AmplitunerElementService(@Autowired SwitchElement amplituner, @Autowired MQTTConnectionService mqttConnectionService) {
        super(amplituner, mqttConnectionService);
    }

    @Override
    protected Command translateCommand(Command command) {
        return getMQTTPublishCommand(command);
    }

    @PostConstruct
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    /**
     * Act on command, it can be an ON/OFF or a different IR command the device will respond to
     * @param command received command
     */
    @Override
    public void commandReceived(Command command) {
        assert command != null;
        LOGGER.log(Level.INFO, "Command received:\t" + command);
        String cmd = command.getValue();
        try {
            if ("ON".equalsIgnoreCase(cmd) || "OFF".equalsIgnoreCase(cmd)) {
                handleSwitchCommand(command);
            } else {
                handleIrCommand(command);
            }
        } catch (OperationNotSupportedException e){
            LOGGER.log(Level.WARNING, e.getMessage());
        }
    }

    /**
     * ON or OFF command received
     * @param command ON or OFF command
     * @throws OperationNotSupportedException
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
     * turn on the device, first flip on the switch, and then send an IR code
     * @param command ON command
     */
    private void handleOnCommand(Command command) {
        getConnectionService().actOnConnection(translateCommand(command));
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                handleIrCommand(command);
            } catch (InterruptedException | OperationNotSupportedException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }).start();
    }

    /**
     * turn off the device, first send an IR code, and then flip off the switch
     * @param command OFF command
     */
    private void handleOffCommand(Command command) throws OperationNotSupportedException {
        handleIrCommand(command);
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                getConnectionService().actOnConnection(translateCommand(command));
            } catch (InterruptedException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
            }
        }).start();
    }

    /**
     * send out an IR command, command gets translated into an IR code,
     * if it's to be repeated n times, add a "n_" suffix to it
     * @param command IR command
     * @throws OperationNotSupportedException if no IR command was found in the IR codes map for the element
     */
    private void handleIrCommand(Command command) throws OperationNotSupportedException{
        String cmd = command.getValue();
        int repeat = 1;
        if(cmd.contains("_")){
            String[] strings = cmd.split("_");
            repeat = Integer.parseInt(strings[0]);
            cmd = strings[1];
        }
        String irCode = getIRCodeForCommand(cmd.toLowerCase());
        if (irCode != null) {
            getConnectionService().actOnConnection(new MQTTCommand(getIrPublishTopic(), (repeat > 1 ? repeat + "_" : "") + irCode));
        } else{
            throw new OperationNotSupportedException("Unable to find an IR code for the command: " + command);
        }
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
}
