package com.piotrak.service.controller;

import com.piotrak.service.elementservice.ElementService;

/**
 * Controller for the elements
 */
public abstract class ElementController extends AbstractController {

    /**
     * Get the element's service
     * @return element's service
     */
    protected abstract ElementService getService();

}
