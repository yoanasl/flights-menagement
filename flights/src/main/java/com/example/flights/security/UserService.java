package com.example.flights.security;

import com.example.flights.entity.User;
import com.example.flights.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.flights.enums.UserRole.ADMIN;
import static com.example.flights.enums.UserRole.USER;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    private void saveAdmin() {
        final User admin = new User ("admin", "admin");
        admin.setGrantedAuthorities (ADMIN.getGrantedAuthority ());
        userRepository.save (admin);
    }



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(email);
        if (!user.isEmpty ()) {
            return new UserDetailsImpl (user.get ());
        }
        else {
            throw  new UsernameNotFoundException ("Email not found "+ email);
        }
    }
}