package com.piotrak.service.logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Logger for displaying logs in the UI, each log is also forwarded to the java.util.logging.Logger
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class WebLogger {

    private Logger logger;

    private String className;

    @Autowired
    private WebLoggerQueue webLoggerQueue;

    public void setUp(String className) {
        this.className = className;
        logger = java.util.logging.Logger.getLogger(className);
    }

    public void log(Level level, String msg){
        logger.log(level, msg);
        webLoggerQueue.addEntry(new WebLoggerEntry(className, level, msg));
    }

    public void log(Level level, String msg, Throwable e){
        logger.log(level, msg, e);
        webLoggerQueue.addEntry(new WebLoggerEntry(className, level, msg + "\t" + e.getMessage()));
    }
}
