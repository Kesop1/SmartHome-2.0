package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;

public interface MQTTCommunication extends Communication {

    String getMQTTPublishTopic();

    String getMQTTSubscribeTopic();

    @PostConstruct
    void setUpElementForMQTT();

    default MQTTCommand getMQTTPublishCommand(Command command){
        assert !StringUtils.isEmpty(getMQTTPublishTopic());
        return new MQTTCommand(getMQTTPublishTopic(), command.getValue());
    }
}
