package com.warehouse.inventory.model;

public class Response {
    private boolean success;
    private Object message;

    public Response(boolean success, Object message) {
        this.success = success;
        this.message = message;
    }
    public Response(){

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getMessage() {
        return message;
    }

    public void setMessage(Object message) {
        this.message = message;
    }
}
