package com.example.flights.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User{
    @Id
    @NotBlank
    @NonNull
    private String email;

    @NonNull
    @NotBlank
    @Size(min = 8)
    private String password;

    private Set<? extends GrantedAuthority> grantedAuthorities;

    public User(String email, String password){
        this.email = email;
        this.password = password;
    }
}