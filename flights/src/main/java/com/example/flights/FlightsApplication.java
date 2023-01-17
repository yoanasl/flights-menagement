package com.example.flights;

import com.example.flights.security.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class FlightsApplication{

    private final UserService userService;

    public FlightsApplication(UserService userService){
        this.userService = userService;
    }

    public static void main(String[] args){
        SpringApplication.run(FlightsApplication.class, args);
    }


}
