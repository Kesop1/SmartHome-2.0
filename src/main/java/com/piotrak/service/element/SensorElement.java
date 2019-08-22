package com.piotrak.service.element;

import com.piotrak.service.action.Readable;
import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;

/**
 * Element representing a sensor
 */
public class SensorElement extends Element implements Readable {

    /**
     * Read value
     */
    private String value = "";

    /**
     * Value unit
     */
    private String unit = "";
    
    public SensorElement(@NotBlank String name) {
        super(name);
    }
    
    public SensorElement(@NotBlank String name, String displayName) {
        super(name, displayName);
    }

    /**
     * Update the values map based on the command received
     * @param command The command with values
     * @throws OperationNotSupportedException when the command value doesn't comply to the standard message
     */
    @Override
    public void actOnCommand(Command command) throws OperationNotSupportedException {
        this.value = command.getValue();
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     * @param value value
     */
    @Override
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String getUnit() {
        return unit;
    }

    /**
     * {@inheritDoc}
     * @param unit unit
     */
    @Override
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
