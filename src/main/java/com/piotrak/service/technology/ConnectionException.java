package com.piotrak.service.technology;

/**
 * Exception thrown when there's something wrong with the connection
 */
public class ConnectionException extends Exception {
    
    public ConnectionException() {
    }
    
    public ConnectionException(String message) {
        super(message);
    }
    
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
