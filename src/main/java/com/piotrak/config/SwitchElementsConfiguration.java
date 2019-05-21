package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.technology.mqtt.MQTTConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class SwitchElementsConfiguration {

    @Autowired
    private MQTTConnection mqttConnection;

    @Bean
    public Map<String, Element> thingsMap(){
        Map<String, Element> thingsMap = new HashMap<>();
        thingsMap.put("TV", tv());
        thingsMap.put("Amplituner", amplituner());
        return thingsMap;
    }


    @Bean
    public SwitchElement tv(){
       return new SwitchElement(mqttConnection);
    }

    @Bean
    public SwitchElement amplituner(){
        return new SwitchElement(mqttConnection);
    }
    
}
