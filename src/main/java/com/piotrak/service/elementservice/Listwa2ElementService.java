package com.piotrak.service.elementservice;

import com.piotrak.service.action.Inactivable;
import com.piotrak.service.element.Element;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.mqtt.MQTTCommand;
import com.piotrak.service.technology.mqtt.MQTTCommunication;
import com.piotrak.service.technology.mqtt.MQTTConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Listwa2 service for communication between systems
 */
@Service("listwa2ElementService")
@ConfigurationProperties("listwa2.device")
public class Listwa2ElementService extends ElementService implements MQTTCommunication {

    @Autowired
    private WebLogger webLogger;

    private String subscribeTopic = "default/status";

    private List<Element> elements = new ArrayList<>();

    private String dhtPublishTopic = "default";

    public Listwa2ElementService(@Autowired SwitchElement tv, @Autowired SwitchElement amplituner, @Autowired SwitchElement speakers,
                                 @Autowired SwitchElement ps3, @Autowired MQTTConnectionService mqttConnectionService) {
        super(tv, mqttConnectionService);
        elements.add(tv);
        elements.add(amplituner);
        elements.add(speakers);
        elements.add(ps3);
    }

    /**
     * Enable or disable the elements based on listwa2's status command
     * @param command Command received
     */
    @Override
    public void commandReceived(@NotNull Command command) {
        if(command.getValue().contains("disconnected")){
            elements.stream().filter(e -> e instanceof Inactivable)
                    .forEach(e -> ((Inactivable)e).setActive(false));
        } else if(command.getValue().contains("connected")){
            elements.stream().filter(e -> e instanceof Inactivable)
                    .forEach(e -> ((Inactivable)e).setActive(true));
        }
    }

    /**
     * Translate the command so it can be published in the element's publishTopic
     * @param command Command to be translated
     * @return MQTT command
     */
    @Override
    protected Command translateCommand(Command command) {
        return getMQTTPublishCommand(command);
    }

    /**
     * {@inheritDoc}
     */
    @PostConstruct
    @Override
    public void setUp() {
        webLogger.setUp(this.getClass().getName());
        super.setWebLogger(webLogger);
        webLogger.log(Level.FINE, "Setting up " + getElement().getName() + " for MQTT Connection");
        assert !StringUtils.isEmpty(getSubscribeTopic());
        ((MQTTConnectionService) getConnectionService()).subscribeToTopic(getSubscribeTopic(), this);
    }

    /**
     * Ask the MQTT device for DHT read-outs update
     */
    @Scheduled(fixedDelay = 1800000L)
    private void getDhtReadOuts(){
        webLogger.log(Level.FINE, "Getting DHT read outs");
        getConnectionService().actOnConnection(new MQTTCommand(getDhtPublishTopic(), "get"));
    }

    public String getSubscribeTopic() {
        return subscribeTopic;
    }

    public void setSubscribeTopic(String subscribeTopic) {
        this.subscribeTopic = subscribeTopic;
    }

    public String getPublishTopic() {
        return "";
    }

    public String getDhtPublishTopic() {
        return dhtPublishTopic;
    }

    public void setDhtPublishTopic(String dhtPublishTopic) {
        this.dhtPublishTopic = dhtPublishTopic;
    }
}
