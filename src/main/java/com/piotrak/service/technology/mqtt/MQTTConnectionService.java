package com.piotrak.service.technology.mqtt;

import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import com.piotrak.service.technology.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("mqttConnectionService")
public class MQTTConnectionService extends ConnectionService {

    private Logger LOGGER = Logger.getLogger("MQTTConnectionService");

    private Map<String, ElementService> elementServiceTopicsMap = new HashMap<>();

    public MQTTConnectionService(@Autowired MQTTConnection mqttConnection) {
        super(mqttConnection);
    }

    @Override
    public void actOnConnection(Command command) {
        try {
            getConnection().send(command);
        } catch (ConnectionException e) {
            LOGGER.log(Level.WARNING, "Unable to send command: " + command, e);
        }
    }

    public void subscribeToTopic(String topic, ElementService elementService){
        LOGGER.log(Level.INFO, "Subscribing " + elementService.getElement().getName() + " to topic: " + topic);
        elementServiceTopicsMap.put(topic, elementService);
        ((MQTTConnection) getConnection()).subscribe(topic);
    }

    @Scheduled(fixedDelay = 1000)
    @Async
    @Override
    public void checkForCommands() {
        LOGGER.log(Level.FINE, "Looking for commands from the MQTT Connection");
        super.checkForCommands();
    }

    @Override
    public void sendCommandToElementService(Command command) {
        ElementService service = elementServiceTopicsMap.get(((MQTTCommand) command).getTopic());
        if(service != null){
            service.commandReceived(command);
        } else{
            LOGGER.log(Level.WARNING, "Unable to localize the ElementService for command: " + command);
        }
    }
}
