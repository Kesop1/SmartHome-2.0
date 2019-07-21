package com.piotrak.service.action;

import java.util.Map;

/**
 * Interface for devices that send out values that can be read and displayed
 */
public interface Readable {

    /**
     * Get the values map
     * @return values map
     */
    Map<String, String> getValues();

    /**
     * Set the values map
     * @param values map of values
     */
    void setValues(Map<String, String> values);
    
}
