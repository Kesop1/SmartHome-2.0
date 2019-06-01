package com.piotrak.service.controller;

import com.piotrak.service.elementservice.ElementService;

public abstract class ElementController extends AbstractController {

    protected abstract ElementService getService();

}
