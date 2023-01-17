package com.example.flights.exception;

public class NoFlightsFoundException extends RuntimeException{
    public NoFlightsFoundException(String message, Throwable cause){
        super(message, cause);
    }

    public NoFlightsFoundException(String message){
        super(message);
    }
}
