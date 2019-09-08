package com.piotrak.service.technology.mqtt;

import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import com.piotrak.service.technology.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 */
@Service("mqttConnectionService")
public class MQTTConnectionService extends ConnectionService {

    @Autowired
    private WebLogger webLogger;

    /**
     * All the topics to be subscribed to
     */
    private Map<String, ElementService> elementServiceTopicsMap = new HashMap<>();

    public MQTTConnectionService(@Autowired MQTTConnection mqttConnection) {
        super(mqttConnection);
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Send command to the MQTT broker
     * @param command Command to be sent
     */
    @Override
    public void actOnConnection(Command command) {
        try {
            getConnection().send(command);
        } catch (ConnectionException e) {
            webLogger.log(Level.WARNING, "Unable to send command: " + command, e);
        }
    }

    /**
     * Subscribe to the topic
     * @param topic Topic to be subscribed to
     * @param elementService Element listening to the topic
     */
    public void subscribeToTopic(String topic, ElementService elementService){
        webLogger.log(Level.INFO, "Subscribing " + elementService.getElement().getName() + " to topic: " + topic);
        elementServiceTopicsMap.put(topic, elementService);
        ((MQTTConnection) getConnection()).subscribe(topic);
    }

    /**
     * Check for the MQTT commands in the queue
     */
    @Scheduled(fixedDelay = 1000)
    @Async
    @Override
    public void checkForCommands() {
        webLogger.log(Level.FINE, "Looking for commands from the MQTT Connection");
        super.checkForCommands();
    }

    /**
     * Send the command to the appropriate elementService
     * @param command Command to be sent
     */
    @Override
    public void sendCommandToElementService(Command command) {
        ElementService service = elementServiceTopicsMap.get(((MQTTCommand) command).getTopic());
        if(service != null){
            service.commandReceived(command);
        } else{
            webLogger.log(Level.WARNING, "Unable to localize the ElementService for command: " + command);
        }
    }
}
