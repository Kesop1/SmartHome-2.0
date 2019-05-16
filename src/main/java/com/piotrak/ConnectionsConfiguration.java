package com.piotrak;

import com.piotrak.service.technology.mqtt.MQTTConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConnectionsConfiguration {
    
    @Bean
    public MQTTConnection mqttConnection(){
        return new MQTTConnection();
    }
    
}
