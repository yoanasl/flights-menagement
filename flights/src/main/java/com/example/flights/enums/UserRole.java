package com.example.flights.enums;

import lombok.AllArgsConstructor;
import lombok.Setter;
import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.flights.enums.Permission.*;

@AllArgsConstructor
public enum UserRole {
    USER(Sets.newHashSet(FLIGHT_READ, FLIGHT_WRITE)),
    ADMIN(Sets.newHashSet(FLIGHT_DELETE,FLIGHT_READ, FLIGHT_WRITE));

    private final Set<Permission> permissions;
    public Set<Permission> getPermissions(){return permissions;}

    public Set<SimpleGrantedAuthority> getGrantedAuthority(){
        Set<SimpleGrantedAuthority> permissions= getPermissions ().stream ()
                .map (permission-> new SimpleGrantedAuthority (permission.getPermission ()))
                .collect(Collectors.toSet());
        permissions.add (new SimpleGrantedAuthority ("ROLE_"+ this.name ()));
        return permissions;
    }
}
