package com.MTDap.commons.controller;

import com.MTDap.commons.model.DTOResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseController {

    protected static final int OK = 200;
    protected static final int ERR = 500;
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);

    protected DTOResponse handleErr(Throwable e) {
        LOGGER.error("Request failed with message {}", e.getMessage());
        DTOResponse response = new DTOResponse();
        response.setCode(ERR);
        response.setMessage(e.getMessage());
        return response;
    }
}