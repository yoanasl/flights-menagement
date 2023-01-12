package com.example.flights.service;

import com.example.flights.dto.CreateFlightDTO;
import com.example.flights.dto.FlightDTO;
import com.example.flights.entity.Flight;

public interface FlightService {

  FlightDTO createFlight(CreateFlightDTO createFlightDTO);
}
