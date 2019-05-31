package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ConnectionException;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MQTTConnection.class)
@TestPropertySource
public class MQTTConnectionServiceTests {

    private static String SUBSCRIBE_TOPIC = "tv.subscribeTopic";

    private static String PUBLISH_TOPIC = "tv.publishTopic";

    private static String HOST = "mqtt.host";

    private static String PORT = "mqtt.port";

    private static String PROTOCOL = "mqtt.protocol";

    @Autowired
    private MQTTConnection mqttConnection;

    @Autowired
    Environment env;

    @Before
    public void connect() throws ConnectionException {
        mqttConnection.setHost(env.getProperty(HOST));
        mqttConnection.setPort(env.getProperty(PORT));
        mqttConnection.setProtocol(env.getProperty(PROTOCOL));
        mqttConnection.connect();
    }

    @After
    public void disconnect(){
        mqttConnection.disconnect();
    }

    @Test
    public void connectTest() {
        assertTrue(mqttConnection.isConnected());
    }

    @Test
    public void disconnectTest() {
        assertTrue(mqttConnection.isConnected());
        mqttConnection.disconnect();
        assertFalse(mqttConnection.isConnected());
    }

    @Test
    public void getInitialElementStateTest() throws ConnectionException, MqttException, InterruptedException {
        assertTrue(mqttConnection.isConnected());
        String topic = env.getProperty(SUBSCRIBE_TOPIC);
        String value = "ON";
        MqttTopic mqttTopic = mqttConnection.getMqttClient().getTopic(topic);

        //new retained message
        MqttMessage message = new MqttMessage(value.getBytes());
        message.setRetained(true);
        mqttTopic.publish(message);

        //disconnect
        mqttConnection.disconnect();
        assertFalse(mqttConnection.isConnected());

        //check if after reconnecting the message arrives
        connect();
        assertTrue(mqttConnection.isConnected());
        mqttConnection.subscribe(topic);
        Thread.sleep(2000);
        Command command = mqttConnection.getCommandQueue().element();
        assertNotNull(command);
        assertEquals(command.getValue(), value);

        //removing retained message
        mqttTopic = mqttConnection.getMqttClient().getTopic(topic);
        message = new MqttMessage();
        message.setRetained(true);
        mqttTopic.publish(message);
        mqttConnection.getCommandQueue().clear();
    }

    @Test
    public void commandFromSubscribedTopic() throws InterruptedException, MqttException {
        assertTrue(mqttConnection.isConnected());
        String topic = env.getProperty(SUBSCRIBE_TOPIC);
        String value = "OFF";
        mqttConnection.subscribe(topic);
        MqttTopic mqttTopic = mqttConnection.getMqttClient().getTopic(topic);
        MqttMessage message = new MqttMessage(value.getBytes());
        mqttTopic.publish(message);

        Thread.sleep(2000);
        Command command = mqttConnection.getCommandQueue().element();
        assertNotNull(command);
        assertEquals(command.getValue(), value);
        mqttConnection.getCommandQueue().clear();
    }

    @Test
    public void publishCommandTest() throws MqttException {
        assertTrue(mqttConnection.isConnected());
        String topic = env.getProperty(PUBLISH_TOPIC);
        String value = "ON";

        //removing retained message
        MqttTopic mqttTopic = mqttConnection.getMqttClient().getTopic(topic);
        MqttMessage message = new MqttMessage("OFF".getBytes());
        mqttTopic.publish(message);
    }
}
