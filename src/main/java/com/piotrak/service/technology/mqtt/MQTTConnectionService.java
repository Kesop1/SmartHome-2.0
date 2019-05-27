package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import com.piotrak.service.technology.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("mqttConnectionService")
public class MQTTConnectionService extends ConnectionService {

    public MQTTConnectionService(@Autowired MQTTConnection mqttConnection) {
        super(mqttConnection);
    }

    @Override
    public void actOnCommand(Command command) {
        try {
            getConnection().send(command);
        } catch (ConnectionException e) {
            e.printStackTrace();//TODO: obsluga wyjatku
        }
    }

    public void subscribeToTopic(String topic){
        ((MQTTConnection) getConnection()).subscribeToTopic(topic);
    }
}
