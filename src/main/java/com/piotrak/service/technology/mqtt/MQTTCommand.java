package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;

import javax.validation.constraints.NotBlank;

/**
 * MQTT {@inheritDoc}
 */
public class MQTTCommand extends Command {

    /**
     * Topic of the MQTT message
     */
    private final String topic;

    public MQTTCommand(@NotBlank String topic, @NotBlank String command) {
        super(command);
        this.topic = topic;
    }
    
    public String getTopic() {
        return topic;
    }

    @Override
    public String toString() {
        return "MQTTCommand{" +
                "value='" + getValue() + "\', " +
                "topic='" + topic + '\'' +
                '}';
    }
}
