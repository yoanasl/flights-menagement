package com.example.flights.service;

import com.example.flights.dto.CreateFlightDTO;
import com.example.flights.entity.Flight;

public interface FlightService {

  Flight createFlight(CreateFlightDTO createFlightDTO);
}
