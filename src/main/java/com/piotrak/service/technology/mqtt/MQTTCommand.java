package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;

public class MQTTCommand extends Command {
    
    private String topic;
    
    private String message;
    
    public MQTTCommand(String topic, String message) {
        this.topic = topic;
        this.message = message;
    }
    
    public String getTopic() {
        return topic;
    }
    
    public String getMessage() {
        return message;
    }
}
