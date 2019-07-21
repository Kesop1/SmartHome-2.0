package com.piotrak.service.action;

import javax.naming.OperationNotSupportedException;

/**
 * Interface for devices that can be switched ON or OFF
 */
public interface Switchable {//TODO: Enum ON i OFF
    //TODO: isSwitched

    /**
     * Switch the element
     * @param command ON or OFF
     * @throws OperationNotSupportedException when a command is incorrect
     */
    void switchElement(String command) throws OperationNotSupportedException;

}
