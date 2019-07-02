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

@Service("listwa1WeatherSensorElementService")
@ConfigurationProperties("listwa1.weather")
public class Listwa1WeatherSensorElementService extends ElementService implements MQTTCommunication {
    
    private Logger LOGGER = Logger.getLogger("Listwa1WeatherSensorElementService");
    
    private String subscribeTopic = "default";
    
    public Listwa1WeatherSensorElementService(@Autowired SensorElement listwa1WeatherSensor, @Autowired MQTTConnectionService mqttConnectionService) {
        super(listwa1WeatherSensor, mqttConnectionService);
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
