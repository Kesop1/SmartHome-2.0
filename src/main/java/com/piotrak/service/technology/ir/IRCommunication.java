package com.piotrak.service.technology.ir;

import java.util.Map;

/**
 * Interface marking elements that communicate using IR signals
 */
public interface IRCommunication {

    /**
     * Map of commands and corresponding IR codes for the device
     * @return map of commands and corresponding IR codes for the device
     */
    Map<String, String> getIRCodesMap();

    /**
     * Retrieve the IR code for the command from the IR codes map
     * @param command command for the element
     * @return IR code for the command, null if not found in the map
     */
    default String getIRCodeForCommand(String command){
        return getIRCodesMap().get(command);
    }

}
