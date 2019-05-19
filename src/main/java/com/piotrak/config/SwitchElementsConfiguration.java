package com.piotrak.config;

import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.mqtt.MQTTConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwitchElementsConfiguration {
    
    @Autowired
    private MQTTConnection mqttConnection;
    
    @Bean
    public SwitchElement tv(){
        return new SwitchElement(mqttConnection);
    }
    
}
