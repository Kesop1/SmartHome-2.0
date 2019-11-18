package com.piotrak.service.elementservice;

import com.piotrak.config.ServicesConfiguration;
import com.piotrak.service.CommandService;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.naming.OperationNotSupportedException;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Templates service for handling group actions and sending them to the appropriate element services
 */
@Service("templateElementService")
@ConfigurationProperties("template")
public class TemplateElementService extends CommandService {

    @Autowired
    private WebLogger webLogger;

    /**
     * Template currently active
     */
    private TemplateElement activeTemplate = null;

    private TemplateElement templateRestOff;

    private TemplateElement templateAllOff;

    private List<TemplateElement> templatesList;

    private ServicesConfiguration servicesConfiguration;

    @Autowired
    public TemplateElementService(TemplateElement templateRestOff, TemplateElement templateAllOff, List<TemplateElement> templatesList,
                                  ServicesConfiguration servicesConfiguration) {
        this.templateRestOff = templateRestOff;
        this.templateAllOff = templateAllOff;
        this.templatesList = templatesList;
        this.servicesConfiguration = servicesConfiguration;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    @Override
    public void commandReceived(@NotNull Command command) {
        switchTemplate(command);
    }

    /**
     * Activate the template, mark the rest as inactive
     * if the "restOff" template is activated, turn all the elements off except for the ones defined in the active template
     * @param command Template's name
     */
    private void switchTemplate(Command command){
        String name = command.getValue();
        if(!checkCommand(name)){
            webLogger.log(Level.WARNING, "Invalid template name: " + name);
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
                    if(template.isActive()) {
                        for (Map.Entry<String, Command> templateCommand : template.getTemplateCommands().entries()) {
                            CommandService service = servicesConfiguration.getServiceMap().get(templateCommand.getKey());
                            if (service != null) {
                                service.commandReceived(templateCommand.getValue());
                            }
                        }
                    }
                } catch (OperationNotSupportedException e) {
                    webLogger.log(Level.WARNING, "Unable to switch template " + template.getName(), e);
                }
            }
        }
    }

    /**
     * Turn all the elements off except for the ones defined in the active template
     * 1. get the list of all commands for elements defined in AllOff template
     * 2. remove from it all the commands from the active template
     * 3. turn off the rest of the elements
     */
    private void restOffTemplate(){
        MultiValuedMap<String, Command> restOffcommands = new ArrayListValuedHashMap<>(templateAllOff.getTemplateCommands());
        if(activeTemplate != null){
            for(Map.Entry<String, Command> activeTemplateCommands : activeTemplate.getTemplateCommands().entries()){
                restOffcommands.remove(activeTemplateCommands.getKey());
            }
            templateRestOff.getTemplateCommands().clear();
            templateRestOff.getTemplateCommands().putAll(restOffcommands);
            try {
                templateRestOff.switchElement("ON");
                for (Map.Entry<String, Command> templateCommand : templateRestOff.getTemplateCommands().entries()) {
                    CommandService service = servicesConfiguration.getServiceMap().get(templateCommand.getKey());
                    if (service != null) {
                        service.commandReceived(templateCommand.getValue());
                    }
                }
            } catch (OperationNotSupportedException e) {
                webLogger.log(Level.WARNING, "Unable to switch template " + activeTemplate.getName(), e);
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
