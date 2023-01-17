package com.example.flights.controller;

import com.example.flights.dto.UserDto;
import com.example.flights.repository.UserRepository;
import com.example.flights.security.UserService;
import com.example.flights.service.RegisterService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping
@AllArgsConstructor
public class UserController {

   private final RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto){
        if(registerService.registerUser (userDto)){
          return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

//    @PostMapping("/register")
//    public UserDto fake(@Valid @RequestBody UserDto userDto){
//
//        return userDto;
//    }

}
