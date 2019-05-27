package com.piotrak.service.technology;

public abstract class ConnectionService {

    private Connection connection;

    public ConnectionService(Connection connection) {
        this.connection = connection;
        try {
            connection.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();//TODO: obsluga wyjatku
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public abstract void actOnCommand(Command command);
}
