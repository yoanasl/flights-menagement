package com.example.flights.exception;

public class DateTimeParseException extends IllegalArgumentException{
    public DateTimeParseException(String message){
        super(message);
    }
}
