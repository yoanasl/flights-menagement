package com.example.flights.service;

import com.example.flights.dto.UserDto;
import com.example.flights.entity.User;
import com.example.flights.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.flights.enums.UserRole.ADMIN;
import static com.example.flights.enums.UserRole.USER;

@Service
public class RegisterService{

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    public RegisterService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public boolean registerUser(UserDto userDto){
        if(userRepository.existsById(userDto.getEmail())){
            return false;
        }
        String encodedPass = passwordEncoder.encode(userDto.getPassword());
        userDto.setPassword(encodedPass);
        User userToCreate = new User();
        userToCreate.setPassword(userDto.getPassword());
        userToCreate.setEmail(userDto.getEmail());
        userToCreate.setGrantedAuthorities(USER.getGrantedAuthority());
        userRepository.save(userToCreate);
        return true;
    }

    @PostConstruct
    public void registerAdmin(){
        if(userRepository.existsById("admin@abv.bg")){
            return;
        }
        User userToCreate = new User();
        userToCreate.setPassword(passwordEncoder.encode("123456"));
        userToCreate.setEmail("admin@abv.bg");
        userToCreate.setGrantedAuthorities(ADMIN.getGrantedAuthority());
        userRepository.save(userToCreate);
    }
}
