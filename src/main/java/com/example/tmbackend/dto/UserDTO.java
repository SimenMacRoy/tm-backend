package com.example.tmbackend.dto;

import com.example.tmbackend.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

public class UserDTO implements UserDetails {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String telephone;
    private String address;
    private String role;

    // Constructeurs
    public UserDTO() {}

    public UserDTO(Integer id, String firstName, String lastName, String email, String telephone, String address, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.role = role;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.telephone = user.getTelephone();
        this.address = user.getAddress();
        this.role = user.getRole();
    }

    // Getters & Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Implémentation des méthodes de UserDetails
    @Override
    public Collection<SimpleGrantedAuthority> getAuthorities() {
        // Retourne les rôles de l'utilisateur sous forme de SimpleGrantedAuthority
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getUsername() {
        // Utilise l'email comme nom d'utilisateur pour l'authentification
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Personnaliser si nécessaire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Personnaliser si nécessaire
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Personnaliser si nécessaire
    }

    @Override
    public boolean isEnabled() {
        return true; // Personnaliser si nécessaire
    }
}
