package com.MTDap.commons.model;

import com.google.gson.JsonObject;

import java.util.HashMap;

public class DTOResponse extends HashMap<String, Object> {
    private static final String MESSAGE = "message";
    private static final String DATA = "data";
    private static final String STATUS = "status";

    public DTOResponse() {
        this.setCode(200);
        this.setData(new JsonObject().toString());
    }

    public DTOResponse(String message, Object data) {
        this.setCode(200);
        this.setMessage(message);
        this.setData(data);
    }

    public DTOResponse(String message) {
        this.setCode(200);
        this.setMessage(message);
    }

    public void setCode(Object code) {
        this.put(STATUS, code);
    }

    public void setMessage(Object message) {
        this.put(MESSAGE, message);
    }

    public void setData(Object data) {
        this.put(DATA, data);
    }

}
