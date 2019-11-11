package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.SensorElement;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;

/**
 * Sensor beans displayed in the weather tile
 */
@Configuration
@EnableConfigurationProperties()
public class SensorsConfiguration {
    
    @Bean
    public List<Element> sensorsList(){
        List<Element> sensorsList = new LinkedList<>();
        sensorsList.add(listwa3HumSensor());
        sensorsList.add(listwa3TempSensor());
        sensorsList.add(listwa2HumSensor());
        sensorsList.add(listwa2TempSensor());
        return sensorsList;
    }
    
    @Bean
    public Element listwa3TempSensor(){
        return new SensorElement("Listwa3TempSensor", "Biurko temp");
    }

    @Bean
    public Element listwa3HumSensor(){
        return new SensorElement("Listwa3HumSensor", "Biurko hum");
    }
    
    @Bean
    public Element listwa2TempSensor(){
        return new SensorElement("Listwa2TempSensor", "TV temp");
    }

    @Bean
    public Element listwa2HumSensor(){
        return new SensorElement("Listwa2HumSensor", "TV hum");
    }
}
