package com.example.tmbackend.dto;

import com.example.tmbackend.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        // Retourne le rôle de l'utilisateur
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // En fonction de la logique métier, tu peux personnaliser cette méthode
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Personnaliser si nécessaire
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Personnaliser si nécessaire
    }

    @Override
    public boolean isEnabled() {
        return true;  // Personnaliser si nécessaire
    }

    public User getUser() {
        return user;
    }
}
