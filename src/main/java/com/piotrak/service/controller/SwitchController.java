package com.piotrak.service.controller;

import javax.naming.OperationNotSupportedException;

class SwitchController {
    
    String getCommand(String cmd) throws OperationNotSupportedException {
        if(!"ON".equalsIgnoreCase(cmd) || !"OFF".equalsIgnoreCase(cmd)){
            throw new OperationNotSupportedException("Invalid command send for Switch Element");
        }
        return cmd.toUpperCase();
    }
}
