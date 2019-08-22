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

/**
 * Listwa1 temperature sensor service for communication between systems
 */
@Service("listwa1TempSensorElementService")
@ConfigurationProperties("listwa1.temp")
public class Listwa1TempSensorElementService extends ElementService implements MQTTCommunication {

    private Logger LOGGER = Logger.getLogger(this.getClass().getName());

    private String subscribeTopic = "default";

    private final String unit = "\u2103";

    public Listwa1TempSensorElementService(@Autowired SensorElement listwa1TempSensor, @Autowired MQTTConnectionService mqttConnectionService) {
        super(listwa1TempSensor, mqttConnectionService);
        listwa1TempSensor.setUnit(unit);
    }
    
    @Override
    public String getPublishTopic() {
        return null;//Nothing to publish
    }
    
    @Override
    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void setUpElementForMQTT() {
        LOGGER.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    /**
     * Sensor element only receives commands
     * @param command Command to be ignored
     * @return null
     */
    @Override
    protected Command translateCommand(Command command) {
        return null;//Nothing to do
    }

    public void setSubscribeTopic(String subscribeTopic) {
        this.subscribeTopic = subscribeTopic;
    }

    public String getUnit() {
        return unit;
    }
}
