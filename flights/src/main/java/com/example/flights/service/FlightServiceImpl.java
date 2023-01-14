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
import java.math.BigInteger;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;
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
            checkForEmptyFields (createFlightDTO);
            if (flightRepository.existsByOrderNumber (new BigInteger (createFlightDTO.getOrderNumber ()))) {
                throw new InvalidRequestException ("Flight with such order number already exists!");
            }
            checkTypeAndDate (createFlightDTO);
            Flight flight = (new Flight (
                    new BigInteger (createFlightDTO.getOrderNumber ()),
                    new BigDecimal (createFlightDTO.getAmount ()),
                    ISOStandartTime (createFlightDTO.getStartDate ()),
                    ISOStandartTime (createFlightDTO.getEndDate ()),
                    Type.valueOf (createFlightDTO.getType ())));

            if (flight.getStartDate ().isAfter (flight.getEndDate ())) {
                throw new UnprocessableEntityException ("Start date must be before end date");
            }
            flightRepository.save (flight);
            return getFlightDTO (flight);
        } catch (InvalidRequestException e) {
            log.error ("Error creating flight: " + e.getMessage (), e);
            throw e;
        } catch (UnprocessableEntityException e) {
            log.error ("Error creating flight: Start date must be before end date" + e.getMessage (), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error ("Error creating flight: Invalid amount: " + createFlightDTO.getAmount (), e);
            throw new UnprocessableEntityException ("Invalid amount: " + createFlightDTO.getAmount (), e);
        } catch (DateTimeParseException e) {
            log.error ("Error creating flight: Invalid date format: " + createFlightDTO.getStartDate (), e);
            throw new UnprocessableEntityException ("Invalid date format: " + createFlightDTO.getStartDate (), e);
        } catch (IllegalArgumentException e) {
            log.error ("Error creating flight: Invalid type: " + createFlightDTO.getType (), e);
            throw new UnprocessableEntityException ("Invalid type: " + createFlightDTO.getType (), e);
        }
    }

    @Override
    public ResponseFlightDTO updateFlight(int id, FlightDTO flightDTO) {
        checkForEmptyFields (flightDTO);
        Flight flight = flightRepository.findById (id)
                .orElseThrow (() -> new NoFlightsFoundException ("No flights found"));
        checkTypeAndDate (flightDTO);
        try {
            flight.setOrderNumber (new BigInteger (flightDTO.getOrderNumber ()));
            flight.setAmount (new BigDecimal (flightDTO.getAmount ()));
            flight.setStartDate (ISOStandartTime (flightDTO.getStartDate ()));
            flight.setEndDate ((ISOStandartTime (flightDTO.getEndDate ())));
            flight.setType (Type.valueOf (flightDTO.getType ()));
            flightRepository.save (flight);
            return getFlightDTO (flight);
        } catch (NoFlightsFoundException e) {
            log.error ("Error updating flights: " + e.getMessage (), e);
            throw e;
        } catch (NumberFormatException e) {
            log.error ("Error updating flight: Invalid amount: " + e.getMessage (), e);
            throw new UnprocessableEntityException ("Invalid amount: " + flightDTO.getAmount ());
        } catch (DateTimeParseException e) {
            log.error ("Error updating flight: Invalid date format:" + e.getMessage (), e);
            throw new UnprocessableEntityException ("Invalid date format: " + flightDTO.getStartDate ());
        }
    }

    @Override
    public List<ResponseFlightDTO> getFlights() throws NoFlightsFoundException {
        try {
            List<ResponseFlightDTO> flightDTOList = flightRepository.findAll ().stream ().map
                    (flight -> getFlightDTO (flight)).toList ();
            if (flightDTOList.isEmpty ()) {
                throw new NoFlightsFoundException ("No flights found");
            }
            return flightDTOList;
        } catch (NoFlightsFoundException e) {
            log.error ("Error retrieving flights: " + e.getMessage (), e);
            throw e;
        }
    }

    @Override
    public void deleteAllFlights() {
        try {
            if (getFlights ().isEmpty ()) {
                throw new NoFlightsFoundException ("No flights found");
            }
            flightRepository.deleteAll ();
        } catch (NoFlightsFoundException e) {
            log.error ("Error deleting flights: " + e.getMessage (), e);
            throw e;
        }
    }

    @Override
    public Map<String, String> getStatistics() {
        try {Map<String, String> statistic = new HashMap<> ();
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
        return statistic;}
        catch (NoFlightsFoundException e ){
            log.error("Error retrieving for statistics flights: " + e.getMessage(), e);
            throw e;
        }
    }

    private void checkTypeAndDate(FlightDTO createFlightDTO) {
     try {
         if (!EnumSet.of (Type.ONE_WAY, Type.RETURN, Type.MULTI_CITY).contains (Type.valueOf (createFlightDTO.getType ()))) {
             throw new UnprocessableEntityException ("Invalid type: " + createFlightDTO.getType ());
         }
         if (ISOStandartTime (createFlightDTO.getStartDate ()).isAfter (ISOStandartTime (createFlightDTO.getEndDate ()))) {
             throw new UnprocessableEntityException ("Start date must be before end date");
         }
     }catch (UnprocessableEntityException e){
         log.error("Error checking date and type for flights: " + e.getMessage(), e);
         throw e;
     }
    }


    private static void checkForEmptyFields(FlightDTO flightDTO) {

     try{   if (flightDTO.getOrderNumber ().isEmpty ()) {
            throw new InvalidRequestException ("Missing orderNumber field");
        }
        if (flightDTO.getAmount ().isEmpty ()) {
            throw new InvalidRequestException ("Missing amount field");
        }
        if (flightDTO.getEndDate ().isEmpty ()) {
            throw new InvalidRequestException ("Missing endDate field");
        }
        if (flightDTO.getStartDate ().isEmpty ()) {
            throw new InvalidRequestException ("Missing startDate field");
        }
        if (flightDTO.getType ().isEmpty ()) {
            throw new InvalidRequestException ("Missing type field");
        }}
     catch (InvalidRequestException e){
         log.error ("Error missing fields ",e);
         throw e;
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

    private Instant ISOStandartTime(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
        Instant date = Instant.from (dateTimeFormatter.parse (time));
        return date;

    }


}
