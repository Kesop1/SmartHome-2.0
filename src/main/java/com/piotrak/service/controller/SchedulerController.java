package com.piotrak.service.controller;

import com.piotrak.service.ScheduledCommandService;
import com.piotrak.service.logger.WebLogger;
import com.piotrak.service.technology.time.ScheduledCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;

/**
 * Controller for the scheduler element
 */
@RestController
@RequestMapping("/scheduler")
public class SchedulerController extends AbstractController {

    private WebLogger webLogger;

    private ScheduledCommandService scheduledCommandService;

    @Autowired
    public SchedulerController(WebLogger webLogger, ScheduledCommandService scheduledCommandService) {
        this.webLogger = webLogger;
        this.scheduledCommandService = scheduledCommandService;
    }

    @PostConstruct
    public void setUp(){
        webLogger.setUp(this.getClass().getName());
    }

    /**
     * Display the scheduler page
     * @return scheduler page
     */
    @GetMapping
    public ModelAndView getScheduler() {
        ModelAndView model = super.getModelAndView();
        model.setViewName("schedulerView");
        model.addObject("scheduledCommands", scheduledCommandService.getScheduledCommands());
        return model;
    }

    /**
     * Schedule a job
     * @return scheduler page
     */
    @PostMapping
    public ModelAndView scheduleJob(@RequestParam String element, @RequestParam String command, @RequestParam String delay, @RequestParam String time) {
        Date date = new Date(System.currentTimeMillis());
        if(!StringUtils.isEmpty(time)){
            String[] hourAndTime = time.split(":");
            int hours = Integer.valueOf(hourAndTime[0]);
            int minutes = Integer.valueOf(hourAndTime[1]);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hours);
            calendar.set(Calendar.MINUTE, minutes);
            if(date.after(calendar.getTime())){
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            date = calendar.getTime();
        } else if(!StringUtils.isEmpty(delay)) {
            int offset = Integer.valueOf(delay);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, offset);
            date = calendar.getTime();
        } else {
            return new ModelAndView("redirect:/scheduler");
        }
        ScheduledCommand scheduledCommand = new ScheduledCommand(date, element, command);
        scheduledCommandService.commandReceived(scheduledCommand);
        return new ModelAndView("redirect:/scheduler");
    }

    /**
     * Remove a job by id
     * @param jobId
     * @return scheduler page
     */
    @PostMapping(path = "/{jobId}")
    public ModelAndView removeScheduledJob(@PathVariable String jobId){
        scheduledCommandService.removeScheduledJob(jobId);
        return new ModelAndView("redirect:/scheduler");
    }
}