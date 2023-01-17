package com.example.flights.repository;

import com.example.flights.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface FlightRepository  extends JpaRepository<Flight, Integer> {
    boolean existsByOrderNumber(Long orderNumber);

   Flight getById(int id);
    List<Flight> findAll();



}
