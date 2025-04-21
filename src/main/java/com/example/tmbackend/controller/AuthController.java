package com.example.tmbackend.controller;

import com.example.tmbackend.dto.CustomUserDetails;
import com.example.tmbackend.dto.UserCredentialsDto;
import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.security.JwtUtil;
import com.example.tmbackend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/task-master/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody UserCredentialsDto userCredentialsDto) {
        System.out.println("Received credentials: " + userCredentialsDto);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userCredentialsDto.getEmail(), userCredentialsDto.getPassword())
        );

        String jwtToken = jwtUtil.generateToken(createUserDTOFromCustomUserDetails((CustomUserDetails) authentication.getPrincipal()));
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDTO userDTO = new UserDTO(
                userDetails.getUser().getId(),
                userDetails.getUser().getFirstName(),
                userDetails.getUser().getLastName(),
                userDetails.getUser().getEmail(),
                userDetails.getUser().getTelephone(),
                userDetails.getUser().getAddress(),
                userDetails.getUser().getRole()
        );

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + jwtToken)
                .body(userDTO);
    }

    private UserDTO createUserDTOFromCustomUserDetails(CustomUserDetails userDetails) {
        return new UserDTO(
                userDetails.getUser().getId(),
                userDetails.getUser().getFirstName(),
                userDetails.getUser().getLastName(),
                userDetails.getUser().getEmail(),
                userDetails.getUser().getTelephone(),
                userDetails.getUser().getAddress(),
                userDetails.getUser().getRole()
        );
    }
}
