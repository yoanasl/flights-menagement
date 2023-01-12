package com.example.flights.service;

import com.example.flights.dto.CreateFlightDTO;
import com.example.flights.entity.Flight;
import com.example.flights.repository.FlightRepository;
import com.example.flights.service.FlightService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final ModelMapper modelMapper;
    private final FlightRepository flightRepository;


    @Override
    public Flight createFlight(CreateFlightDTO createFlightDTO) {

        Flight flight= modelMapper.map(createFlightDTO, Flight.class);
        flightRepository.save(flight);
        return flight;
//        FlightDTO flightDTO= modelMapper.map(flight,FlightDTO.class);
//        return flightDTO;
    }
}
