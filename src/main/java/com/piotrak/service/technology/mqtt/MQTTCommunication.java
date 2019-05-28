package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;

import javax.annotation.PostConstruct;

public interface MQTTCommunication extends Communication {

    String getMQTTPublishTopic();

    String getMQTTSubscribeTopic();

    @PostConstruct
    void subscribeToMQTTTopic();

    default MQTTCommand getMQTTPublishCommand(Command command){
        return new MQTTCommand(getMQTTPublishTopic(), command.getValue());
    }
}
