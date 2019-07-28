package com.piotrak.service.controller;

import com.piotrak.service.elementservice.ElementService;
import com.piotrak.service.technology.Command;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the elements
 */
public abstract class ElementController extends AbstractController {

    /**
     * Get the element's service
     * @return element's service
     */
    protected abstract ElementService getService();

    /**
     * Handle the command received, send it to the appropriate service and redirect to the main screen
     * @param cmd command received
     * @return main screen
     */
    protected ModelAndView handleCommand(Command cmd){
        getService().commandReceived(cmd);
        return super.getModelAndView();
    }

}
