package com.example.flights.controller;

import com.example.flights.dto.CreateFlightDTO;
import com.example.flights.dto.FlightDTO;
import com.example.flights.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api-flights/flights")
public class FlightController {
   private final FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightDTO> createFlight(@RequestBody CreateFlightDTO createFlightDTO) {
        return ResponseEntity.ok().body(flightService.createFlight(createFlightDTO));
    }
}
