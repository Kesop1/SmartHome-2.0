package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.HorizontalSeparatorElement;
import com.piotrak.service.element.SwitchElement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ThingsConfiguration {

    @Bean
    public Map<String, Element> thingsMap(){
        Map<String, Element> thingsMap = new LinkedHashMap<>();
        thingsMap.put(tv().getName(), tv());
        thingsMap.put(amplituner().getName(), amplituner());
        thingsMap.put(horizontalSeparator().getName(), horizontalSeparator());
        return thingsMap;
    }

    @Bean
    public SwitchElement tv(){
       return new SwitchElement("TV");
    }

    @Bean
    public SwitchElement amplituner(){
        return new SwitchElement("Amplituner");
    }

    @Bean
    public HorizontalSeparatorElement horizontalSeparator() {
        return new HorizontalSeparatorElement("");
    }
    
}
