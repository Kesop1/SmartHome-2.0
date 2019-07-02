package com.piotrak.service.elementservice;

import com.piotrak.service.element.SensorElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("listwa2WeatherSensorElementService")
@ConfigurationProperties("listwa2.weather")
public class Listwa2WeatherSensorElementService extends ElementService implements MQTTCommunication {
    
    private Logger LOGGER = Logger.getLogger("Listwa2WeatherSensorElementService");
    
    private String subscribeTopic = "default";
    
    public Listwa2WeatherSensorElementService(@Autowired SensorElement listwaWeatherSensor, @Autowired MQTTConnectionService mqttConnectionService) {
        super(listwaWeatherSensor, mqttConnectionService);
    }
    
    @Override
    public String getPublishTopic() {
        return null;//Nothing to publish
    }
    
    @Override
    public String getSubscribeTopic() {
        return subscribeTopic;
    }
    
    @PostConstruct
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }
    
    @Override
    protected Command translateCommand(Command command) {
        return null;//Nothing to do
    }
}
