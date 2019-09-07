package com.piotrak.config;

import com.piotrak.service.CommandService;
import com.piotrak.service.element.Element;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ir.IRCommand;
import com.piotrak.service.technology.time.ConditionalCommand;
import com.piotrak.service.technology.time.DelayedCommand;
import com.piotrak.service.technology.web.WebCommand;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Templates that group actions, displayed in the templates tile
 */
@Configuration
@EnableConfigurationProperties()
public class TemplatesConfiguration {
    
    private Logger LOGGER = Logger.getLogger("TemplatesConfiguration");

    @Autowired
    private ServicesConfiguration servicesConfiguration;

    private WebCommand commandOff = new WebCommand("OFF");

    private WebCommand commandOn = new WebCommand("ON");

    @Bean
    public List<Element> templatesList(){
        List<Element> list = new LinkedList<>();
        list.add(templateAllOff());
        list.add(templateRadio());
        list.add(templatePc());
        list.add(templateMovie());
        list.add(templateWork());
        list.add(templateRestOff());
        return list;
    }

    @Bean//TODO: to chyba nie musi być Bean
    public TemplateElement templateAllOff(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(servicesConfiguration.getServiceMap().get("TV"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("Amplituner"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("Speakers"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("PS3"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("Laptop"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("Desk"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("PC"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("PCScreen"), commandOff);
        actions.put(servicesConfiguration.getServiceMap().get("PCSpeakers"), commandOff);
        return new TemplateElement("allOff", "Pawsinoga", actions);
    }

    @Bean
    public TemplateElement templatePc(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(servicesConfiguration.getServiceMap().get("PC"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("conditional"), new ConditionalCommand((e)-> ((SwitchElement)e).isOn(), new WebCommand("audio-pc"), servicesConfiguration.getServiceMap().get("PC"), 60000));
        actions.put(servicesConfiguration.getServiceMap().get("Desk"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("PCScreen"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("PCSpeakers"), commandOn);//TODO: ustaw odpoeiednie wyjscia na switchu
        return new TemplateElement("pc", "Pececiarz", actions);
    }

    @Bean
    public TemplateElement templateRadio() {
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(servicesConfiguration.getServiceMap().get("Amplituner"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("Speakers"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("delayed"), new DelayedCommand(2000, new IRCommand("4"), servicesConfiguration.getServiceMap().get("Amplituner")));
        return new TemplateElement("radio", "Meloman", actions);
    }

    @Bean
    public TemplateElement templateRestOff(){
        return new TemplateElement("restOff", "Reszta off", new ArrayListValuedHashMap<>());
    }//TODO: jakies akcje tu sa potrzebne

    @Bean
    public TemplateElement templateMovie(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(servicesConfiguration.getServiceMap().get("PC"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("conditional"), new ConditionalCommand((e)-> ((SwitchElement)e).isOn(), new WebCommand("audio-tv"), servicesConfiguration.getServiceMap().get("PC"), 60000));
        actions.put(servicesConfiguration.getServiceMap().get("Amplituner"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("delayed"), new DelayedCommand(2000, new IRCommand("2"), servicesConfiguration.getServiceMap().get("Amplituner")));
        actions.put(servicesConfiguration.getServiceMap().get("Speakers"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("TV"), commandOn);
        return new TemplateElement("movie", "Kinoman", actions);
    }

    @Bean
    public TemplateElement templateWork(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(servicesConfiguration.getServiceMap().get("Laptop"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("Desk"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("PCScreen"), commandOn);
        actions.put(servicesConfiguration.getServiceMap().get("PCSpeakers"), commandOn);//TODO: ustaw odpowiednie wyjscia na switchu
        return new TemplateElement("work", "BioRobot", actions);
    }



}
