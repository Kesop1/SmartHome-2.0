package com.piotrak.service.technology.ir;

import java.util.Map;

public interface IRCommunication {

    Map<String, String> getIRCodesMap();

    default String getIRCodeForCommand(String command){
        return getIRCodesMap().get(command);
    }

}
