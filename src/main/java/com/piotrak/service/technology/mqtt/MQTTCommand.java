package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;

public class MQTTCommand extends Command {
    
    private final String topic;

    public MQTTCommand(String topic, String command) {
        super(command);
        this.topic = topic;
    }
    
    public String getTopic() {
        return topic;
    }

}
