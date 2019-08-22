package com.piotrak.service.action;

/**
 * Interface for devices that send out values that can be read and displayed
 */
public interface Readable {

    /**
     * Get the value
     * @return value
     */
    String getValue();

    /**
     * Set the values map
     * @param value read value
     */
    void setValue(String value);

    /**
     * Get unit of the read value
     * @return unit of the read
     */
    String getUnit();

    /**
     * Set unit of the read value
     * @param unit unit
     */
    void setUnit(String unit);
    
}
