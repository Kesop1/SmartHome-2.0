package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("mqttConnection")
public class MQTTConnection extends Connection {

    @Value("${connection.mqtt.host}")
    private String host;
    
    @Value("${connection.mqtt.port}")
    private String port;
    
    @Value("${connection.mqtt.protocol}")
    private String protocol;

    private MqttClient mqttClient;

    private Set<String> subscribeTopics = new HashSet<>();

    @Override
    public void connect() throws ConnectionException {
        System.out.println("Connect");//TODO
        String uri = protocol + "://" + host + ":" + port;
        try {
            mqttClient = new MqttClient(uri, MqttClient.generateClientId(), new MemoryPersistence());
            setCallback();
            mqttClient.connect();
            subscribeTopics.forEach(this::subscribe);
        } catch (MqttException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    @Override
    public boolean isConnected() {
        if (mqttClient != null) {
            return mqttClient.isConnected();
        }
        return false;
    }

    @Override
    public void disconnect() {
        if (isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
//                LOGGER.error("Exception occurred while disconnecting MQTTModuleConnection", e);//TODO
            }
        }
    }
    
    @Override
    public void send(Command command) throws ConnectionException {
        if(!(command instanceof MQTTCommand)){
            throw new ConnectionException("Unable to send command through MQTTConnection");
        }
        MQTTCommand mqttCommand = (MQTTCommand) command;
        MqttTopic mqttTopic = mqttClient.getTopic(mqttCommand.getTopic());
        try {
            mqttTopic.publish(new MqttMessage(mqttCommand.getValue().getBytes()));
        } catch (MqttException e) {
//            LOGGER.error("Error occurred while publishing MQTT command", e);//TODO
        }
    }
    
    @Override
    public Command receive() throws ConnectionException {
        //dealt with inside setCallback() -> messageArrived() method
        return null;
    }

    void subscribe(String topic){
        subscribeTopics.add(topic);
        try {
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
//            LOGGER.error("Error while subscribing to topic " + topic, e);//TODO
        }
    }

    private void setCallback() {
        if (mqttClient != null) {
            mqttClient.setCallback(new MqttCallback() {

                @Override
                public void connectionLost(Throwable throwable) {
                    if(!isConnected()){
                        try {
                            connect();
                        } catch (ConnectionException e) {
                            e.printStackTrace();//TODO
                        }
                    }
                    if(!isConnected()){
//                        LOGGER.error("MQTTClient got disconnected", throwable);//TODO
                    }
                }

                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    String message = new String(mqttMessage.getPayload());
//                    LOGGER.info("MQTTMessage received in topic " + s + ": " + message);
                    MQTTCommand command = new MQTTCommand(topic, message);
                    getCommandQueue().add(command);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                    LOGGER.info("MQTTMessage successfully sent");//TODO
                }
            });
        }
    }
}
