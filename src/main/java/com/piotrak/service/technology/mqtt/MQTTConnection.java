package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Connection using the MQTT technology
 */
@Component("mqttConnection")
@ConfigurationProperties("mqtt")
public class MQTTConnection extends Connection {

    private Logger LOGGER = Logger.getLogger("MQTTConnection");

    /**
     * MQTT broker host address
     */
    private String host = "0.0.0.0";

    /**
     * MQTT broker host port
     */
    private String port = "0";

    /**
     * MQTT broker host protocol
     */
    private String protocol = "tcp";

    /**
     * MqttClient that acts as a middle man between the application and MQTT broker
     */
    private MqttClient mqttClient;

    /**
     * Topics to listen to
     */
    private Set<String> subscribeTopics = new HashSet<>();

    /**
     * Connect to the MQTT broker
     * @throws ConnectionException when unable to connect
     */
    @Override
    public void connect() throws ConnectionException {//TODO: aplikacja nie wstaje gdy nie można się połączyć z MQTT
        String uri = protocol + "://" + host + ":" + port;
        LOGGER.log(Level.INFO, "Connecting to " + uri);
        try {
            mqttClient = new MqttClient(uri, MqttClient.generateClientId(), new MemoryPersistence());
            setCallback();
            mqttClient.connect();
            LOGGER.log(Level.INFO, "Connected successfully");
            subscribeTopics.forEach(this::subscribe);
        } catch (MqttException e) {
            throw new ConnectionException(e.getMessage());
        }
    }

    /**
     * Is the application connected to the broker
     * @return true if the application is connected to the broker
     */
    @Override
    public boolean isConnected() {
        if (mqttClient != null) {
            return mqttClient.isConnected();
        }
        return false;
    }

    /**
     * Disconnect from the MQTT broker
     */
    @Override
    public void disconnect() {
        LOGGER.log(Level.INFO, "Disconnecting");
        if (isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                LOGGER.log(Level.WARNING, "Exception occurred while disconnecting", e);
            }
        }
    }

    /**
     * Send a message to the MQTT broker
     * @param command Command to be sent
     * @throws ConnectionException when an error occurs during sending or an incorrect message is received
     */
    @Override
    public void send(Command command) throws ConnectionException {
        LOGGER.log(Level.INFO, "Sending command: " + command);
        if(!(command instanceof MQTTCommand)){
            throw new ConnectionException("Unable to send command through MQTTConnection");
        }
        MQTTCommand mqttCommand = (MQTTCommand) command;
        MqttTopic mqttTopic = mqttClient.getTopic(mqttCommand.getTopic());
        try {
            mqttTopic.publish(new MqttMessage(mqttCommand.getValue().getBytes()));
        } catch (MqttException e) {
            throw new ConnectionException("Error occurred while publishing MQTT command", e);
        }
    }

    /**
     * Not used
     * @return null
     * @throws ConnectionException not thrown
     */
    @Override
    public Command receive() throws ConnectionException {
        //dealt with inside setCallback() -> messageArrived() method
        return null;
    }

    /**
     * Listen for messages in the topic
     * @param topic Topic to be subscribed to
     */
    void subscribe(String topic){
        subscribeTopics.add(topic);
        try {
            mqttClient.subscribe(topic);
        } catch (MqttException e) {
            LOGGER.log(Level.WARNING, "Unable to subscribe to topic " + topic, e);
        }
    }

    /**
     * Methods for MQTT connection
     */
    private void setCallback() {
        if (mqttClient != null) {
            mqttClient.setCallback(new MqttCallback() {

                /**
                 * When connection to the broker is lost retry then log the message
                 * @param throwable Error causing the disconnection
                 */
                @Override
                public void connectionLost(Throwable throwable) {
                    LOGGER.log(Level.SEVERE, "Connection lost! Retrying", throwable);
                    if(!isConnected()){
                        try {
                            connect();
                        } catch (ConnectionException e) {
                            LOGGER.log(Level.SEVERE, "Unable to connect", e);
                        }
                    }
                    if(!isConnected()){
                        LOGGER.log(Level.WARNING, "Unable to reconnect, connection disabled");
                    }
                }

                /**
                 * Message arrived from the subscribed topic, add it to the messages queue
                 * @param topic Topic of the message
                 * @param mqttMessage Message content
                 */
                @Override
                public void messageArrived(String topic, MqttMessage mqttMessage) {
                    String message = new String(mqttMessage.getPayload());
                    MQTTCommand command = new MQTTCommand(topic, message);
                    LOGGER.log(Level.INFO, "Command received: " + command);
                    getCommandQueue().add(command);
                }

                /**
                 * Message sent was delivered successfully
                 * @param iMqttDeliveryToken token of the delivered message
                 */
                @Override
                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
                    LOGGER.log(Level.FINE, "Message successfully sent: " + iMqttDeliveryToken);
                }
            });
        }
    }

    public MqttClient getMqttClient() {
        return mqttClient;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }
}
