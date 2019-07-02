package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.SensorElement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

@Configuration
@EnableConfigurationProperties()
public class SensorsConfiguration {
    
    @Bean
    public List<Element> sensorsList(){
        List<Element> sensorsList = new LinkedList<>();
        sensorsList.add(listwa1WeatherSensor());
        sensorsList.add(listwa2WeatherSensor());
        return sensorsList;
    }
    
    @Bean
    public Element listwa1WeatherSensor(){
        return new SensorElement("Listwa1WeatherSensor", "Biurko");
    }
    
    @Bean
    public Element listwa2WeatherSensor(){
        return new SensorElement("Listwa2WeatherSensor", "TV");
    }
}
