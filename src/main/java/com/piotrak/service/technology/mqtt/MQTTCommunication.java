package com.piotrak.service.technology.mqtt;

public interface MQTTCommunication {

    String getPublishTopic();

    default MQTTCommand createPublishCommand(String cmd){
        return new MQTTCommand(getPublishTopic(), cmd);
    }
}
