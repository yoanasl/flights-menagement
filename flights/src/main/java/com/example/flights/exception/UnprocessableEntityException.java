package com.example.flights.exception;

public class UnprocessableEntityException extends RuntimeException{
    public UnprocessableEntityException(String message, Throwable cause){
        super(message, cause);
    }

    public UnprocessableEntityException(String message){
        super(message);
    }
}
