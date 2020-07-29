package com.warehouse.inventory.exception;

public class ProductDuplicatedException extends RuntimeException{
    public ProductDuplicatedException(String message){
        super(message);
    }
}
