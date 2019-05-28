package com.piotrak.service.technology.mqtt;

import com.piotrak.service.service.ElementService;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import com.piotrak.service.technology.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("mqttConnectionService")
public class MQTTConnectionService extends ConnectionService {

    private Map<String, ElementService> elementServiceTopicsMap = new HashMap<>();

    public MQTTConnectionService(@Autowired MQTTConnection mqttConnection) {
        super(mqttConnection);
    }

    @Override
    public void actOnConnection(Command command) {
        try {
            getConnection().send(command);
        } catch (ConnectionException e) {
            e.printStackTrace();//TODO: obsluga wyjatku
        }
    }

    public void subscribeToTopic(String topic, ElementService elementService){
        elementServiceTopicsMap.put(topic, elementService);
        ((MQTTConnection) getConnection()).subscribe(topic);
    }

    @Scheduled(fixedDelay = 1000)
    @Async
    @Override
    public void checkForCommands() {
        super.checkForCommands();
    }

    @Override
    public void sendCommandToElementService(Command command) {
        ElementService service = elementServiceTopicsMap.get(((MQTTCommand) command).getTopic());
        if(service != null){
            service.commandReceived(command);
        }
    }
}
