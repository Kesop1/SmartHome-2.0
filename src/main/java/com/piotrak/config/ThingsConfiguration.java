package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.HorizontalSeparatorElement;
import com.piotrak.service.element.SwitchElement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties()
public class ThingsConfiguration {

    @Bean
    public Map<String, Element> thingsMap(){
        Map<String, Element> thingsMap = new LinkedHashMap<>();
        thingsMap.put(tv().getName(), tv());
        thingsMap.put(amplituner().getName(), amplituner());
        thingsMap.put(speakers().getName(), speakers());
        thingsMap.put(ps3().getName(), ps3());
        thingsMap.put(vacuum().getName(), vacuum());
        thingsMap.put(horizontalSeparator().getName(), horizontalSeparator());
        thingsMap.put(pc().getName(), pc());
        thingsMap.put(pcScreen().getName(), pcScreen());
        thingsMap.put(pcSpeakers().getName(), pcSpeakers());
        thingsMap.put(desk().getName(), desk());
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
    public SwitchElement ps3(){
        return new SwitchElement("PS3");
    }

    @Bean
    public SwitchElement speakers(){
        return new SwitchElement("Speakers", "Głośniki");
    }

    @Bean
    public SwitchElement vacuum(){
        return new SwitchElement("Vacuum", "Odkurzacz");
    }

    @Bean
    public SwitchElement desk(){
        return new SwitchElement("Desk", "Biurko");
    }

    @Bean
    public SwitchElement pc(){
        return new SwitchElement("PC");
    }

    @Bean
    public SwitchElement pcScreen(){
        return new SwitchElement("PCScreen", "Monitor");
    }

    @Bean
    public SwitchElement pcSpeakers(){
        return new SwitchElement("PCSpeakers", "Głośniki PC");
    }

    @Bean
    public HorizontalSeparatorElement horizontalSeparator() {
        return new HorizontalSeparatorElement("Line");
    }
    
}
