package com.piotrak.service.elementservice;

import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.technology.Command;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("templateElementService")
@ConfigurationProperties("template")
public class TemplateElementService {

    private Logger LOGGER = Logger.getLogger("TemplateElementService");

    private TemplateElement activeTemplate = null;

    @Autowired
    private TemplateElement templateRestOff;

    @Autowired
    private TemplateElement templateAllOff;

    @Autowired
    private List<TemplateElement> templatesList;

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

    private void restOffTemplate(){
        Map<ElementService, Command> restOffcommands = new HashMap<>(templateAllOff.getElementCommandMap());
        if(activeTemplate != null){
            for(Map.Entry activeCommands : activeTemplate.getElementCommandMap().entrySet()){
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

    private boolean checkCommand(String name){
        for (TemplateElement template : templatesList) {
            if(template.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }
}
