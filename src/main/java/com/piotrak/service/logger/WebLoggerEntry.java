package com.piotrak.service.logger;

import com.google.api.client.util.DateTime;

import java.util.logging.Level;

/**
 * Web log
 */
public class WebLoggerEntry {

    private DateTime dateTime;

    private String className;

    private Level level;

    private String message;

    public WebLoggerEntry(String className, Level level, String message) {
        this.dateTime = new DateTime(System.currentTimeMillis());
        this.className = className;
        this.level = level;
        this.message = message;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getClassName() {
        return className;
    }

    public Level getLevel() {
        return level;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return dateTime + "\t " + className + "\t " + level + "\t " + message;
    }
}
