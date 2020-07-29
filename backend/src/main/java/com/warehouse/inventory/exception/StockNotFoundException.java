package com.warehouse.inventory.exception;

public class StockNotFoundException extends RuntimeException{
    public StockNotFoundException(String message){
        super(message);
    }
}
