package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Communication;

import javax.annotation.PostConstruct;

/**
 * Interface marking elements that communicate using MQTT technology
 */
public interface MQTTCommunication extends Communication {

    /**
     * get the topic MQTT commands will be pushed to
     * @return publish topic
     */
    String getPublishTopic();

    /**
     * get the topic MQTT commands will be received from
     * @return subscribe topic
     */
    String getSubscribeTopic();

    /**
     * subscribe to the MQTT topic
     */
    @PostConstruct
    void setUpElementForMQTT();

    /**
     * create a MQTT publish command form a command of a different type
     * @param command command to be sent
     * @return MQTT command to be published
     */
    default MQTTCommand getMQTTPublishCommand(Command command){
        return new MQTTCommand(getPublishTopic(), command.getValue());
    }
}
