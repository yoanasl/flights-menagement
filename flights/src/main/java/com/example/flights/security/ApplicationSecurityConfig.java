package com.example.flights.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.flights.enums.UserRole.ADMIN;
import static com.example.flights.enums.UserRole.USER;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class ApplicationSecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests ()
//                .requestMatchers ("/").permitAll ()
//                .requestMatchers (HttpMethod.POST,"/api-flights/flights").permitAll ()
                .requestMatchers (HttpMethod.DELETE,"/api-flights/flights").hasRole (ADMIN.name ())
                .anyRequest ()
                .authenticated ()
                .and ()
                .httpBasic ().and().csrf ().disable ();
        return http.build ();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user = User.withUsername ("user")
                .password (passwordEncoder.encode ("password"))
                .roles (USER.name ())
                .build ();

        UserDetails admin = User.withUsername ("admin")
                .password (passwordEncoder.encode ("admin"))
                .roles (ADMIN.name ())
                .build ();

        return new InMemoryUserDetailsManager (user, admin);
    }


}
