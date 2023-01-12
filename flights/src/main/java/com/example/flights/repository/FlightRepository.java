package com.example.flights.repository;

import com.example.flights.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FlightRepository  extends JpaRepository<Flight, Integer> {
//    Optional<Flight> findById(String id);
//    Optional<Flight> findBy(String number);
}
