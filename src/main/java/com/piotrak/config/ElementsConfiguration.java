package com.piotrak.config;

import com.piotrak.service.element.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class ElementsConfiguration {

    @Autowired
    private List<Element> thingsList;

    @Autowired
    private List<Element> templatesList;
    
    @Autowired
    private List<Element> sensorsList;

    @Bean
    public Map<String, List<Element>> elementsMap(){
        Map<String, List<Element>> elementsMap = new LinkedHashMap<>();
        elementsMap.put("thingsList", thingsList);
        elementsMap.put("templatesList", templatesList);
        elementsMap.put("sensorsList", sensorsList);
        return elementsMap;
    }

}
