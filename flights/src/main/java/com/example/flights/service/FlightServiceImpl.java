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
@AllArgsConstructor
public class FlightServiceImpl implements FlightService {
    private final FlightRepository flightRepository;

    @Override
    public ResponseFlightDTO createFlight(FlightDTO createFlightDTO) throws UnprocessableEntityException {
        checkForValidType (createFlightDTO);
        if (flightRepository.existsByOrderNumber (new BigInteger (createFlightDTO.getOrderNumber ()))) {
            throw new InvalidRequestException ("Flight with such order number already exists!");
        }
        try {
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
        } catch (NumberFormatException e) {
            throw new UnprocessableEntityException ("Invalid amount: " + createFlightDTO.getAmount ());
        } catch (DateTimeParseException e) {
            throw new UnprocessableEntityException ("Invalid date format: " + createFlightDTO.getStartDate ());
        } catch (IllegalArgumentException e) {
            throw new UnprocessableEntityException ("Invalid type: " + createFlightDTO.getType ());
        }
    }


    @Override
    public ResponseFlightDTO updateFlight(int id, FlightDTO flightDTO) {
        checkForEmptyFields (flightDTO);
        Flight flight = flightRepository.findById (id)
                .orElseThrow (() -> new NoFlightsFoundException ("No flights found"));
        try {
            var startDate = ISOStandartTime (flightDTO.getStartDate ());
            var endDate = ISOStandartTime (flightDTO.getEndDate ());
            if (startDate.isAfter (endDate)) {
                throw new UnprocessableEntityException ("Start date must be before end date");
            }
            flight.setOrderNumber (new BigInteger (flightDTO.getOrderNumber ()));
            flight.setAmount (new BigDecimal (flightDTO.getAmount ()));
            flight.setStartDate (startDate);
            flight.setEndDate (endDate);
            checkForValidType (flightDTO);
            flight.setType (Type.valueOf (flightDTO.getType ()));
            flightRepository.save (flight);
            return getFlightDTO (flight);
        } catch (NumberFormatException e) {
            throw new UnprocessableEntityException ("Invalid amount: " + flightDTO.getAmount ());
        } catch (DateTimeParseException e) {
            throw new UnprocessableEntityException ("Invalid date format: " + flightDTO.getStartDate ());
        }
    }

    private static void checkForValidType(FlightDTO flightDTO) {
        if (!EnumSet.of (Type.ONE_WAY, Type.RETURN, Type.MULTI_CITY).contains (Type.valueOf (flightDTO.getType ()))) {
            throw new UnprocessableEntityException ("Invalid type: " + flightDTO.getType ());
        }
    }

    private static void checkForEmptyFields(FlightDTO flightDTO) {

        if (flightDTO.getOrderNumber ().isEmpty ()) {
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

    @Override
    public List<ResponseFlightDTO> getFlights() throws NoFlightsFoundException {
        List<ResponseFlightDTO> flightDTOList = flightRepository.findAll ().stream ().map
                (flight -> getFlightDTO (flight)).toList ();
        if (flightDTOList.isEmpty ()) {
            throw new NoFlightsFoundException ("No flights found");
        }
        return flightDTOList;
    }

    @Override
    public void deleteAllFlights() {
        if (getFlights ().isEmpty ()) {
            throw new NoFlightsFoundException ("No flights found");
        }
        flightRepository.deleteAll ();
    }

    @Override
    public Map<String, String> getStatistics() {
        Map<String, String> statistic = new HashMap<> ();
        List<Double> amounts = flightRepository.findAll ().stream ()
                .map (f -> (f.getAmount ()).doubleValue ()).sorted ().toList ();
        if (amounts.isEmpty ()) {
            throw new NoFlightsFoundException ("No flights found");
        }
        double sum = amounts.stream ().mapToDouble (Double::doubleValue).sum ();
        statistic.put ("sum", Double.toString (sum));
        statistic.put ("avg", Double.toString (sum / amounts.size ()));
        statistic.put ("max", Double.toString (amounts.get (amounts.size () - 1)));
        statistic.put ("min", Double.toString (amounts.get (0)));
        statistic.put ("count", Integer.toString (amounts.size ()));
        return statistic;
    }

    private Instant ISOStandartTime(String time) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_INSTANT;
        Instant date = Instant.from (dateTimeFormatter.parse (time));
        return date;

    }


}
