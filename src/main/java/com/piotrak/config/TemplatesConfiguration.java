package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.elementservice.*;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.web.WebCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties()
public class TemplatesConfiguration {

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

    @Bean
    public TemplateElement templateAllOff(){//TODO: autowire wszystkich elementów, w serwisie wywołuj actOnCommand na każdym w mapie
        Map<ElementService, Command> actions = new HashMap<>();
        actions.put(tvElementService, commandOff);
        actions.put(amplitunerElementService, commandOff);
        actions.put(ps3ElementService, commandOff);
        actions.put(speakersElementService, commandOff);
        actions.put(laptopElementService, commandOff);
        actions.put(vacuumElementService, commandOff);
        actions.put(deskElementService, commandOff);
        actions.put(pcElementService, commandOff);
        actions.put(pcScreenElementService, commandOff);
        actions.put(pcSpeakersElementService, commandOff);
        return new TemplateElement("allOff", "Pawsinoga", actions);
    }

    @Bean
    public TemplateElement templatePc(){
        Map<ElementService, Command> actions = new HashMap<>();
        actions.put(pcElementService, commandOn);
        actions.put(deskElementService, commandOn);
        actions.put(pcScreenElementService, commandOn);
        actions.put(pcSpeakersElementService, commandOn);//TODO: ustaw odpoeiednie wyjscia na switchu
        return new TemplateElement("pc", "Pececiarz", actions);
    }

    @Bean
    public TemplateElement templateRadio() {
        Map<ElementService, Command> actions = new HashMap<>();
        actions.put(amplitunerElementService, commandOn);
        actions.put(speakersElementService, commandOn);
        return new TemplateElement("radio", "Meloman", actions);
    }

    @Bean
    public TemplateElement templateRestOff(){
        return new TemplateElement("restOff", "Reszta off", new HashMap<>());
    }//TODO: jakies akcje tu sa potrzebne

    @Bean
    public TemplateElement templateMovie(){
        Map<ElementService, Command> actions = new HashMap<>();
        actions.put(pcElementService, commandOn);
        actions.put(pcScreenElementService, commandOff);
        actions.put(pcSpeakersElementService, commandOff);
        actions.put(amplitunerElementService, commandOn);
        actions.put(tvElementService, commandOn);
        actions.put(speakersElementService, commandOn);//TODO: ustaw odpowiedni program na amlitunerze i glosniki na pc
        return new TemplateElement("movie", "Kinoman", actions);
    }

    @Bean
    public TemplateElement templateWork(){
        Map<ElementService, Command> actions = new HashMap<>();
        actions.put(laptopElementService, commandOn);
        actions.put(deskElementService, commandOn);//TODO: ustaw odpowiednie wyjscie na switchu
        return new TemplateElement("work", "Pracuś", actions);
    }

    @Autowired
    public TvElementService tvElementService;

    @Autowired
    public AmplitunerElementService amplitunerElementService;

    @Autowired
    public PS3ElementService ps3ElementService;

    @Autowired
    public SpeakersElementService speakersElementService;

    @Autowired
    public LaptopElementService laptopElementService;

    @Autowired
    public VacuumElementService vacuumElementService;

    @Autowired
    public DeskElementService deskElementService;

    @Autowired
    public PCElementService pcElementService;

    @Autowired
    public PCScreenElementService pcScreenElementService;

    @Autowired
    public PCSpeakersElementService pcSpeakersElementService;

}
