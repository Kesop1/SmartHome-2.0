package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.HorizontalSeparatorElement;
import com.piotrak.service.element.SwitchElement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableConfigurationProperties()
public class ThingsConfiguration {

    @Bean
    public List<Element> thingsList(){
        List<Element> thingsList = new LinkedList<>();
        thingsList.add(tv());
        thingsList.add(amplituner());
        thingsList.add(ps3());
        thingsList.add(vacuum());
        thingsList.add(horizontalSeparator());
        thingsList.add(pc());
        thingsList.add(pcScreen());
        thingsList.add(pcSpeakers());
        thingsList.add(desk());
        thingsList.add(laptop());
        return thingsList;
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
    public SwitchElement laptop(){
        return new SwitchElement("Laptop");
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
