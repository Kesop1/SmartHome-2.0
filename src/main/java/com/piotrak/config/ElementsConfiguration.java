package com.piotrak.config;

import com.piotrak.service.element.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ElementsConfiguration {

    @Autowired
    private Map<String, Element> thingsMap;

    @Bean
    public Map<String, Map<String, Element>> elementsMap(){
        Map<String, Map<String, Element>> elementsMap = new LinkedHashMap<>();
        elementsMap.put("thingsMap", thingsMap);
        return elementsMap;
    }

}
