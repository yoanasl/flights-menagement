package com.example.flights.service;

import com.example.flights.dto.FlightDTO;
import com.example.flights.exception.InvalidRequestException;
import com.example.flights.exception.UnprocessableEntityException;
import com.example.flights.dto.ResponseFlightDTO;
import com.example.flights.exception.NoFlightsFoundException;

import java.util.List;
import java.util.Map;

public interface FlightService {

  ResponseFlightDTO createFlight(FlightDTO createFlightDTO);

  ResponseFlightDTO updateFlight(int id, FlightDTO flightDTO);

  List<ResponseFlightDTO> getFlights() throws NoFlightsFoundException;
  void deleteAllFlights();
   Map<String, String> getStatistics();
}
