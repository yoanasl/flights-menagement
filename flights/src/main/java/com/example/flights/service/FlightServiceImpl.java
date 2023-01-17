package com.example.flights.service;

import com.example.flights.dto.FlightDTO;
import com.example.flights.dto.ResponseFlightDTO;
import com.example.flights.entity.Flight;
import com.example.flights.enums.Type;
import com.example.flights.exception.DateTimeParseException;
import com.example.flights.exception.InvalidRequestException;
import com.example.flights.exception.NoFlightsFoundException;
import com.example.flights.exception.UnprocessableEntityException;
import com.example.flights.repository.FlightRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public ResponseFlightDTO createFlight(FlightDTO createFlightDTO) throws UnprocessableEntityException {
        try {
            if (flightRepository.existsByOrderNumber (Long.valueOf (createFlightDTO.getOrderNumber ()))) {
                throw new InvalidRequestException ("Flight with such order number already exists!");
            }
            checkDate (createFlightDTO);
            Flight flight = (new Flight (
                    Long.valueOf (createFlightDTO.getOrderNumber ()),
                    new BigDecimal (createFlightDTO.getAmount ()),
                    ISOStandardTime (createFlightDTO.getStartDate ()),
                    ISOStandardTime (createFlightDTO.getEndDate ()),
                    Type.valueOf (createFlightDTO.getType ())));
            flightRepository.save (flight);
            return getFlightDTO (flight);
        } catch (InvalidRequestException e) {
            log.error ("Error creating flight: " + e.getMessage ());
            throw e;
        } catch (NumberFormatException e) {
            log.error ("Error creating flight: Invalid amount/order number: " + e.getMessage ());
            throw new UnprocessableEntityException ("Invalid amount/order number: " + e.getMessage ());
        } catch (DateTimeParseException e) {
            log.error ("Error creating flight: " + e.getMessage ());
            throw new UnprocessableEntityException ("Invalid date format: " + createFlightDTO.getStartDate ());
        } catch (IllegalArgumentException e) {
            log.error ("Error creating flight: Invalid type: " + createFlightDTO.getType ());
            throw new UnprocessableEntityException ("Invalid type: " + createFlightDTO.getType ());
        }
    }

    @Override
    public ResponseFlightDTO updateFlight(int id, FlightDTO flightDTO) {
        try {
            Flight flight = flightRepository.findById (id)
                    .orElseThrow (() -> new NoFlightsFoundException ("No flights found"));

            checkDate (flightDTO);
            flight.setOrderNumber (Long.valueOf (flightDTO.getOrderNumber ()));
            flight.setAmount (new BigDecimal (flightDTO.getAmount ()));
            flight.setStartDate (ISOStandardTime (flightDTO.getStartDate ()));
            flight.setEndDate ((ISOStandardTime (flightDTO.getEndDate ())));
            flight.setType (Type.valueOf (flightDTO.getType ()));
            flightRepository.save (flight);
            return getFlightDTO (flight);
        } catch (NoFlightsFoundException e) {
            log.error ("Error updating flights: ");
            throw e;
        } catch (NumberFormatException e) {
            log.error ("Error creating flight: Invalid amount/order number: " + e.getMessage ());
            throw new UnprocessableEntityException ("Invalid amount/order number: " + e.getMessage ());
        } catch (DateTimeParseException e) {
            log.error ("Error updating flight: " + e.getMessage ());
            throw new UnprocessableEntityException ("Invalid date format: " + flightDTO.getStartDate ());
        } catch (IllegalArgumentException e) {
            log.error ("Error creating flight: Invalid type: " + flightDTO.getType ());
            throw new UnprocessableEntityException ("Invalid type: " + flightDTO.getType ());
        }
    }

    @Override
    public List<ResponseFlightDTO> getFlights() throws NoFlightsFoundException {
        try{
            List<ResponseFlightDTO> flightDTOList = flightRepository.findAll ().stream ().map
                    (flight -> getFlightDTO (flight)).toList ();
            if (flightDTOList.isEmpty ()) {
                throw new NoFlightsFoundException ("No flights found");
            }
            return flightDTOList;
        }catch (NoFlightsFoundException e ){
            log.error ("Error retrieving flights: "+ e.getMessage ());
            throw e;
        }

    }

    @Override
    public void deleteAllFlights() {
       try {
            getFlights ();
            flightRepository.deleteAll ();
        }catch (NoFlightsFoundException e){
           log.error ("Error deleting flights: "+e.getMessage ());
           throw e;
       }
    }

    @Override
    public Map<String, String> getStatistics() {
        try {
            Map<String, String> statistic = new HashMap<> ();
            List<Double> amounts = flightRepository.findAll ().stream ()
                    .map (f -> (f.getAmount ()).doubleValue ()).sorted ().toList ();
            if (amounts.isEmpty ()) {
                throw new NoFlightsFoundException ("No flights found");
            }
            double sum = amounts.stream ().mapToDouble (aDouble -> aDouble.doubleValue ()).sum ();
            statistic.put ("sum", Double.toString (sum));
            statistic.put ("avg", Double.toString (sum / amounts.size ()));
            statistic.put ("max", Double.toString (amounts.get (amounts.size () - 1)));
            statistic.put ("min", Double.toString (amounts.get (0)));
            statistic.put ("count", Integer.toString (amounts.size ()));
            return statistic;
        } catch (NoFlightsFoundException e) {
            log.error ("Error retrieving flights for statistics: " + e.getMessage ());
            throw e;
        }
    }

    private void checkDate(FlightDTO createFlightDTO) {
        if (ISOStandardTime (createFlightDTO.getStartDate ())
                .isAfter (ISOStandardTime (createFlightDTO.getEndDate ()))) {
            throw new DateTimeParseException ("Start date must be before end date");
        }
    }

    private static ResponseFlightDTO getFlightDTO(Flight flight) {
        return new ResponseFlightDTO (flight.getId (),
                flight.getOrderNumber ().toString (),
                flight.getAmount ().toPlainString (),
                flight.getStartDate ().toString (),
                flight.getEndDate ().toString (),
                flight.getType ().toString ());
    }


    private Instant ISOStandardTime(String time) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
            Instant date = Instant.from (dateTimeFormatter.parse (time));
            return date;
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException ("Invalid date!");
        }
    }


}
