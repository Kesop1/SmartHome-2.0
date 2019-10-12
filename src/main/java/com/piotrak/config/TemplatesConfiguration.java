package com.piotrak.config;

import com.piotrak.service.element.Element;
import com.piotrak.service.element.TemplateElement;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.Command;
import com.piotrak.service.technology.ir.IRCommand;
import com.piotrak.service.technology.time.DelayedCommand;
import com.piotrak.service.technology.web.WebCommand;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;

/**
 * Templates that group actions, displayed in the templates tile
 */
@Configuration
@EnableConfigurationProperties()
public class TemplatesConfiguration {

    @Autowired
    private WebLogger webLogger;

    @Autowired
    private ServicesConfiguration servicesConfiguration;

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

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
        list.add(templateMusic());
        list.add(templateRestOff());
        return list;
    }

    @Bean
    public TemplateElement templateAllOff(){
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("TV", commandOff);
        actions.put("Amplituner", commandOff);
        actions.put("Speakers", commandOff);
        actions.put("PS3", commandOff);
        actions.put("Laptop", commandOff);
        actions.put("Desk", commandOff);
        actions.put("PC", commandOff);
        actions.put("PCScreen", commandOff);
        actions.put("PCSpeakers", commandOff);
        return new TemplateElement("allOff", "Pawsinoga", actions);
    }

    @Bean
    public TemplateElement templatePc(){
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("PC", commandOn);
//        actions.put("conditional", new ConditionalCommand((e)-> ((SwitchElement)e).isOn(), new WebCommand("audio-pc"), "PC"), 60000));//TODO
        actions.put("delayed", new DelayedCommand(2000, new WebCommand("audio-pc"), "PC"));
        actions.put("delayed", new DelayedCommand(60000, new WebCommand("audio-pc"), "PC"));
        actions.put("Desk", commandOn);
        actions.put("PCScreen", commandOn);
        actions.put("PCSpeakers", commandOn);//TODO: ustaw odpoeiednie wyjscia na switchu
        return new TemplateElement("pc", "Pececiarz", actions);
    }

    @Bean
    public TemplateElement templateRadio() {
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("Amplituner", commandOn);
        actions.put("Speakers", commandOn);
        actions.put("delayed", new DelayedCommand(2000, new IRCommand("4"), "Amplituner"));
        return new TemplateElement("radio", "Meloman", actions);
    }

    @Bean
    public TemplateElement templateRestOff(){
        return new TemplateElement("restOff", "Reszta off", new ArrayListValuedHashMap<>());
    }//TODO: jakies akcje tu sa potrzebne

    @Bean
    public TemplateElement templateMovie(){
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("PC", commandOn);
//        actions.put("conditional", new ConditionalCommand((e)-> e.isOn(), new WebCommand("audio-tv"), "PC"), 60000));//TODO
        actions.put("Amplituner", commandOn);
        actions.put("delayed", new DelayedCommand(2000, new IRCommand("2"), "Amplituner"));
        actions.put("delayed", new DelayedCommand(6000, new WebCommand("audio-tv"), "PC"));
        actions.put("delayed", new DelayedCommand(60000, new WebCommand("audio-tv"), "PC"));
        actions.put("Speakers", commandOn);
        actions.put("TV", commandOn);
        return new TemplateElement("movie", "Kinoman", actions);
    }

    @Bean
    public TemplateElement templateWork(){
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("Laptop", commandOn);
        actions.put("Desk", commandOn);
        actions.put("PCScreen", commandOn);
        actions.put("PCSpeakers", commandOn);//TODO: ustaw odpowiednie wyjscia na switchu
        return new TemplateElement("work", "BioRobot", actions);
    }

    @Bean
    public TemplateElement templateMusic(){
        MultiValuedMap<String, Command> actions = new ArrayListValuedHashMap<>();
        actions.put("Amplituner", commandOn);
        actions.put("Speakers", commandOn);
        actions.put("delayed", new DelayedCommand(2000, new IRCommand("3"), "Amplituner"));
        actions.put("delayed", new DelayedCommand(1000, new WebCommand("audio-amp"), "PC"));
        return new TemplateElement("music", "Muzoman", actions);
    }

}
