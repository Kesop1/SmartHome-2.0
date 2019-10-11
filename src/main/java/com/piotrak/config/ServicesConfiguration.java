package com.piotrak.config;

import com.piotrak.service.CommandService;
import com.piotrak.service.ConditionalCommandService;
import com.piotrak.service.DelayedCommandService;
import com.piotrak.service.elementservice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * A bean with all the services in a map
 */
@Configuration
public class ServicesConfiguration {

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

    private Map<String, CommandService> serviceMap = new HashMap<>();

    @PostConstruct
    public void init(){
        serviceMap.put(tvElementService.getElement().getName(), tvElementService);
        serviceMap.put(amplitunerElementService.getElement().getName(), amplitunerElementService);
        serviceMap.put(speakersElementService.getElement().getName(), speakersElementService);
        serviceMap.put(ps3ElementService.getElement().getName(), ps3ElementService);
        serviceMap.put(laptopElementService.getElement().getName(), laptopElementService);
        serviceMap.put(deskElementService.getElement().getName(), deskElementService);
        serviceMap.put(pcElementService.getElement().getName(), pcElementService);
        serviceMap.put(pcScreenElementService.getElement().getName(), pcScreenElementService);
        serviceMap.put(pcSpeakersElementService.getElement().getName(), pcSpeakersElementService);
        serviceMap.put("delayed", delayedCommandService);
        serviceMap.put("conditional", conditionalCommandService);
    }

    public Map<String, CommandService> getServiceMap() {
        return serviceMap;
    }
}
