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
@TestPropertySource(locations = {"/MQTTConnectionServiceTests.properties"})
public class MQTTConnectionServiceTests {

    @Autowired
    private MQTTConnection mqttConnection;

    @Autowired
    Environment env;

    @Before
    public void connect() throws ConnectionException {
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
        String topic = env.getProperty("mqtt.topic.subscribe.tv");
        String value = "ON";

        //removing retained message
        MqttTopic mqttTopic = mqttConnection.getMqttClient().getTopic(topic);
        MqttMessage message = new MqttMessage();
        message.setRetained(true);
        mqttTopic.publish(message);

        //new retained message
        message = new MqttMessage(value.getBytes());
        message.setRetained(true);
        mqttTopic.publish(message);

        //disconnect
        mqttConnection.disconnect();
        assertFalse(mqttConnection.isConnected());

        //check if after reconnecting the message arrives
        mqttConnection.connect();
        assertTrue(mqttConnection.isConnected());
        mqttConnection.subscribe(topic);
        Thread.sleep(2000);
        Command command = mqttConnection.getCommandQueue().element();
        assertNotNull(command);
        assertEquals(command.getValue(), value);
    }

    @Test
    public void checkIfElementIsGettingSubscribedTest(){

    }

    @Test
    public void commandFromUnsubscribedTopic(){

    }

    @Test
    public void commandFromSubscribedTopic(){

    }

    @Test
    public void publishCommandTest(){

    }
}
