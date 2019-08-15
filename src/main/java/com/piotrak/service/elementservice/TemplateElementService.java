package com.piotrak.service.elementservice;

import com.piotrak.service.CommandService;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.technology.Command;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Templates service for handling group actions and sending them to the appropriate element services
 */
@Service("templateElementService")
@ConfigurationProperties("template")
public class TemplateElementService {

    private Logger LOGGER = Logger.getLogger("TemplateElementService");

    /**
     * Template currently active
     */
    private TemplateElement activeTemplate = null;

    @Autowired
    private TemplateElement templateRestOff;

    @Autowired
    private TemplateElement templateAllOff;

    @Autowired
    private List<TemplateElement> templatesList;

    /**
     * Activate the template, mark the rest as inactive
     * if the "restOff" template is activated, turn all the elements off except for the ones defined in the active template
     * @param name Template's name
     */
    public void switchTemplate(String name){
        if(!checkCommand(name)){
            LOGGER.log(Level.WARNING, "Invalid template name: " + name);
            return;
        }
        if("restOff".equalsIgnoreCase(name)){
            restOffTemplate();
        } else {
            for (TemplateElement template : templatesList) {
                String cmd = "OFF";
                if (name.equalsIgnoreCase(template.getName())) {
                    cmd = "ON";
                    activeTemplate = template;
                }
                try {
                    template.switchElement(cmd);
                } catch (OperationNotSupportedException e) {
                    LOGGER.log(Level.WARNING, "Unable to switch template " + template.getName(), e);
                }
            }
        }
    }

    /**
     * Turn all the elements off except for the ones defined in the active template
     */
    private void restOffTemplate(){
        MultiValuedMap<CommandService, Command> restOffcommands = new ArrayListValuedHashMap<>(templateAllOff.getElementCommandMap());
        if(activeTemplate != null){
            for(Map.Entry activeCommands : activeTemplate.getElementCommandMap().entries()){
                restOffcommands.remove(activeCommands.getKey());
            }
            templateRestOff.getElementCommandMap().clear();
            templateRestOff.getElementCommandMap().putAll(restOffcommands);
            try {
                templateRestOff.switchElement("ON");
            } catch (OperationNotSupportedException e) {
                LOGGER.log(Level.WARNING, "Unable to switch template " + activeTemplate.getName(), e);
            }
        }
    }

    /**
     * Check if the command received has a corresponding template name
     * @param name Name of the template
     * @return true if template was found
     */
    private boolean checkCommand(String name){
        for (TemplateElement template : templatesList) {
            if(template.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
}
