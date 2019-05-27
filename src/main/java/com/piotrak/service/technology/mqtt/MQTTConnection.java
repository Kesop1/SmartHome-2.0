package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("mqttConnection")
public class MQTTConnection extends Connection {

    @Value("${connection.mqtt.host}")
    private String host;
    
    @Value("${connection.mqtt.port}")
    private String port;
    
    @Value("${connection.mqtt.protocol}")
    private String protocol;

    @Override
    public void connect() throws ConnectionException {
        System.out.println("Connect");//TODO
    }
    
    @Override
    public void disconnect() {
        System.out.println("Disconnect");//TODO
    }
    
    @Override
    public void send(Command command) throws ConnectionException {
        if(!(command instanceof MQTTCommand)){
            throw new ConnectionException("Unable to send command through MQTTConnection");
        }
        System.out.println("Send command");//TODO
    }
    
    @Override
    public Command receive() throws ConnectionException {
        System.out.println("Receive command");//TODO
        return null;
    }

    public void subscribeToTopic(String topic){
        System.out.println("Subscribe to " + topic);//TODO
    }
}
