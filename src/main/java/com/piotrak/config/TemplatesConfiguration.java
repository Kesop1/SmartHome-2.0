package com.piotrak.config;

import com.piotrak.service.CommandService;
import com.piotrak.service.ConditionalCommandService;
import com.piotrak.service.DelayedCommandService;
import com.piotrak.service.element.Element;
import com.piotrak.service.element.SwitchElement;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.elementservice.*;
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
        actions.put(tvElementService, commandOff);
        actions.put(amplitunerElementService, commandOff);
        actions.put(speakersElementService, commandOff);
        actions.put(ps3ElementService, commandOff);
        actions.put(laptopElementService, commandOff);
        actions.put(deskElementService, commandOff);
        actions.put(pcElementService, commandOff);
        actions.put(pcScreenElementService, commandOff);
        actions.put(pcSpeakersElementService, commandOff);
        return new TemplateElement("allOff", "Pawsinoga", actions);
    }

    @Bean
    public TemplateElement templatePc(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(pcElementService, commandOn);
        actions.put(conditionalCommandService, new ConditionalCommand((e)-> ((SwitchElement)e).isOn(), new WebCommand("audio-pc"), pcElementService, 60000));
        actions.put(deskElementService, commandOn);
        actions.put(pcScreenElementService, commandOn);
        actions.put(pcSpeakersElementService, commandOn);//TODO: ustaw odpoeiednie wyjscia na switchu
        return new TemplateElement("pc", "Pececiarz", actions);
    }

    @Bean
    public TemplateElement templateRadio() {
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(amplitunerElementService, commandOn);
        actions.put(speakersElementService, commandOn);
        actions.put(delayedCommandService, new DelayedCommand(2000, new IRCommand("4"), amplitunerElementService));
        return new TemplateElement("radio", "Meloman", actions);
    }

    @Bean
    public TemplateElement templateRestOff(){
        return new TemplateElement("restOff", "Reszta off", new ArrayListValuedHashMap<>());
    }//TODO: jakies akcje tu sa potrzebne

    @Bean
    public TemplateElement templateMovie(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(pcElementService, commandOn);
        actions.put(conditionalCommandService, new ConditionalCommand((e)-> ((SwitchElement)e).isOn(), new WebCommand("audio-tv"), pcElementService, 60000));
        actions.put(amplitunerElementService, commandOn);
        actions.put(delayedCommandService, new DelayedCommand(2000, new IRCommand("2"), amplitunerElementService));
        actions.put(speakersElementService, commandOn);
        actions.put(tvElementService, commandOn);
        return new TemplateElement("movie", "Kinoman", actions);
    }

    @Bean
    public TemplateElement templateWork(){
        MultiValuedMap<CommandService, Command> actions = new ArrayListValuedHashMap<>();
        actions.put(laptopElementService, commandOn);
        actions.put(deskElementService, commandOn);
        actions.put(pcScreenElementService, commandOn);
        actions.put(pcSpeakersElementService, commandOn);//TODO: ustaw odpowiednie wyjscia na switchu
        return new TemplateElement("work", "BioRobot", actions);
    }

    @Autowired
    public TvElementService tvElementService;

    @Autowired
    public AmplitunerElementService amplitunerElementService;

    @Autowired
    public SpeakersElementService speakersElementService;

    @Autowired
    public PS3ElementService ps3ElementService;
    
    @Autowired
    public LaptopElementService laptopElementService;

    @Autowired
    public DeskElementService deskElementService;

    @Autowired
    public PCElementService pcElementService;

    @Autowired
    public PCScreenElementService pcScreenElementService;

    @Autowired
    public PCSpeakersElementService pcSpeakersElementService;

    @Autowired
    public DelayedCommandService delayedCommandService;

    @Autowired
    private ConditionalCommandService conditionalCommandService;

}
