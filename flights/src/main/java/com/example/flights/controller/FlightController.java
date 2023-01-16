package com.example.flights.controller;

import com.example.flights.dto.FlightDTO;
import com.example.flights.dto.ResponseFlightDTO;
import com.example.flights.exception.InvalidRequestException;
import com.example.flights.exception.NoFlightsFoundException;
import com.example.flights.exception.UnprocessableEntityException;
import com.example.flights.service.FlightService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api-flights")
public class FlightController {
    private final FlightService flightService;

    //TODO remove the ResponseEntity<FlightDTO>
    @PostMapping("/flights")
    public ResponseEntity<ResponseFlightDTO> createFlight(@Valid @RequestBody FlightDTO createFlightDTO) {
        try {
            flightService.createFlight (createFlightDTO);
            return new ResponseEntity<> (HttpStatus.CREATED);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        } catch (UnprocessableEntityException e) {
            return new ResponseEntity<> (HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @GetMapping("/flights")
    public ResponseEntity<List<ResponseFlightDTO>> getAllFlights() {
        try {
            List<ResponseFlightDTO> flights = flightService.getFlights ();
            return new ResponseEntity<> (flights, HttpStatus.OK);
        } catch (NoFlightsFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<String> hello(){
        return  ResponseEntity.ok ("Hello");
    }
    @GetMapping("/statistics")
    public ResponseEntity getStatistics() {
        try {
            return ResponseEntity.ok ().body (flightService.getStatistics ());
        } catch (NoFlightsFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/flights/{id}")
    public ResponseEntity<ResponseFlightDTO> updateFlight(@PathVariable int id, @RequestBody FlightDTO flightDTO) {
        try {
            ResponseFlightDTO updatedFlight = flightService.updateFlight (id, flightDTO);
            return new ResponseEntity<> (updatedFlight, HttpStatus.OK);
        } catch (UnprocessableEntityException e) {
            return new ResponseEntity<> (HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (NoFlightsFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        } catch (InvalidRequestException e) {
            return new ResponseEntity<> (HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/flights")
    public ResponseEntity deleteAllFlights() {
        try {
            flightService.deleteAllFlights ();
            return new ResponseEntity<> (HttpStatus.NO_CONTENT);
        }catch (NoFlightsFoundException e) {
            return new ResponseEntity<> (HttpStatus.NOT_FOUND);
        }
    }
}
