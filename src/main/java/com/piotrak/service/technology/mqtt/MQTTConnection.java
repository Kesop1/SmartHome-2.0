package com.piotrak.service.technology.mqtt;

import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.Connection;
import com.piotrak.service.technology.ConnectionException;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

public class MQTTConnection implements Connection {
    
    private List<String> subscriptions = new ArrayList<>();
    
    @Value("${connection.mqtt.host}")
    private String host;
    
    @Value("${connection.mqtt.port}")
    private String port;
    
    @Value("${connection.mqtt.protocol}")
    private String protocol;
    
    public MQTTConnection() {
    }
    
    public MQTTConnection(List<String> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
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
        System.out.println("Send command");//TODO
    }
    
    @Override
    public Command receive() throws ConnectionException {
        System.out.println("Receive command");//TODO
        return null;
    }
}
