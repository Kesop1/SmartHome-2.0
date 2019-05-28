package com.piotrak.service.technology;

public abstract class ConnectionService {

    private Connection connection;

    public ConnectionService(Connection connection) {
        this.connection = connection;
        connect();
    }

    protected Connection getConnection(){
        return connection;
    }

    public abstract void actOnConnection(Command command);

    public void checkForCommands(){
        Command command;
        do {
            command = getConnection().getCommandQueue().poll();
            if (command != null) {
                sendCommandToElementService(command);
            }
        } while (command != null);
    }

    private void connect(){
        try {
            connection.connect();
        } catch (ConnectionException e) {
            e.printStackTrace();//TODO: obsluga wyjatku
        }
    }

    public abstract void sendCommandToElementService(Command command);
}
