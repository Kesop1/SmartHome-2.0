package com.piotrak.service.element;

import com.piotrak.service.action.Readable;
import com.piotrak.service.technology.Command;

import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * Element representing a sensor
 */
public class SensorElement extends Element implements Readable {

    /**
     * Map of values read
     */
    private Map<String, String> values = new HashMap<>();
    
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
        String[] commands = command.getValue().split("=");
        if(commands.length<2){
            throw new OperationNotSupportedException("Invalid sensor command received: " + command);
        }
        this.values.put(commands[0], commands[1]);
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public Map<String, String> getValues() {
        return values;
    }

    /**
     * {@inheritDoc}
     * @param values map of values
     */
    @Override
    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
