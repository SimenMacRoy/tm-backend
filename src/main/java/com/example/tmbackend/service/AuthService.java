package com.example.tmbackend.service;

import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.UserRepository;
import com.example.tmbackend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;  // Service pour gérer la génération du token JWT

    @Autowired
    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    // Méthode de connexion qui renvoie un token JWT
    public String login(String email, String password) throws NotFoundException {
        User user = userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new NotFoundException("Email ou mot de passe incorrect."));

        // Générer un token JWT
        return jwtUtil.generateToken(new UserDTO(user));
    }

    public UserDTO loginAndGetDTO(String email, String password) throws NotFoundException {
        User user = userRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password))
                .orElseThrow(() -> new NotFoundException("Email ou mot de passe incorrect."));

        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getTelephone(),
                user.getAddress(),
                user.getRole()
        );
    }
}
