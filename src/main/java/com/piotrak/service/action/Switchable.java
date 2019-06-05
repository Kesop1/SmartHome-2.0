package com.piotrak.service.action;

import javax.naming.OperationNotSupportedException;

public interface Switchable {

    void switchElement(String command) throws OperationNotSupportedException;

}
