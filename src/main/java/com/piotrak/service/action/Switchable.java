package com.piotrak.service.action;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.SwitchElement;

import javax.naming.OperationNotSupportedException;

public interface Switchable {

    default void switchElement(Element element, String command) throws OperationNotSupportedException {
        if(element instanceof SwitchElement) {
            if ("ON".equalsIgnoreCase(command) || "OFF".equalsIgnoreCase(command)) {
                ((SwitchElement) element).setOn("ON".equalsIgnoreCase(command));
            }else{
                throw new OperationNotSupportedException("Invalid command: '" + command + "' sent for : " + element.getName());
            }
        }
    }

}
