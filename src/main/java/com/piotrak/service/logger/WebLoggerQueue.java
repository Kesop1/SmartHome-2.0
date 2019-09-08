package com.piotrak.service.logger;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;

/**
 * Queue for storing logs for the UI
 */
@Component("webLoggerQueue")
@ConfigurationProperties("weblogger")
public class WebLoggerQueue {

    /**
     * Number of logs to be stored
     */
    private int size = 100;

    private String logLevel = "INFO";

    /**
     * Minimal level of log to be stored
     */
    private Level level;

    private Queue<WebLoggerEntry> webLoggerEntries = new ArrayDeque<>();

    /**
     * Add the web log entry to the queue if it's not a lower level of what is set
     * if queue max size is reached, remove the oldest log
     * @param entry
     */
    public void addEntry(WebLoggerEntry entry){
        if(level == null) {
            setLevel(Level.parse(logLevel));
        }
        if(level.intValue() > entry.getLevel().intValue() ){
            return;
        }
        webLoggerEntries.offer(entry);
        while (webLoggerEntries.size() > size){
            webLoggerEntries.poll();
        }
    }

    public List<WebLoggerEntry> getLogs(){
        return new ArrayList<>(webLoggerEntries);
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public int getSize() {
        return size;
    }

    public Level getLevel() {
        return level;
    }
}
