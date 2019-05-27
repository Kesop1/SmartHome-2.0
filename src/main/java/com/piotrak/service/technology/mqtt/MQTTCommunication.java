package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;

import javax.annotation.PostConstruct;

public interface MQTTCommunication extends Communication {

    String getPublishTopic();

    String getSubscribeTopic();

    @PostConstruct
    void subscribeToTopic(String topic);

    default MQTTCommand getPublishCommand(Command command){
        return new MQTTCommand(getPublishTopic(), command.getValue());
    }
}
