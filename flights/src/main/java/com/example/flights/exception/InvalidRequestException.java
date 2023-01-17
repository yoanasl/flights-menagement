package com.example.flights.exception;

public class InvalidRequestException extends RuntimeException{
    public InvalidRequestException(String message, Throwable cause){
        super(message, cause);
    }

    public InvalidRequestException(String message){
        super(message);
    }
}