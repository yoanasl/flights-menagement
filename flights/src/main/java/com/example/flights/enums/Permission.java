package com.example.flights.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Permission {
    FLIGHT_READ("flight:read"),
    FLIGHT_WRITE("flight:write"),
    FLIGHT_UPDATE("flight:update"),
    FLIGHT_DELETE("flight:delete");

    private final String permission;
}
