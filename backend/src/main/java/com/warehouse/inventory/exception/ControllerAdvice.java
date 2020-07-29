package com.warehouse.inventory.exception;

import com.warehouse.inventory.model.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ExceptionHandler({
            InvalidFileTypeException.class,
            InvalidNumberException.class,
            LocationNotFoundException.class,
            ProductNotFoundException.class,
            StockNotFoundException.class
    })
    public ResponseEntity handleUserBadRequestException(Exception exc){
        Response error = new Response();
        error.setSuccess(false);
        error.setMessage(exc.getMessage());
        return new ResponseEntity<Response>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity handleException(Exception exc){
        Response error = new Response();
        error.setSuccess(false);
        error.setMessage(exc.getMessage());
        return new ResponseEntity<Response>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
