package com.example.tmbackend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tmbackend.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

@Component
public class JwtUtil {

    @Getter
    @Value("${tchuilem.jwt.prefix}")
    private String tokenPrefix;

    @Getter
    @Value("${tchuilem.jwt.header}")
    private String tokenHeader;

    @Value("${tchuilem.jwt.secret-key}")  // Injection de la clé secrète depuis application.properties
    private String tokenSecretKey;

    @Value("${tchuilem.jwt.expiration-millis}")  // Injection du temps d'expiration du token depuis application.properties
    private long tokenExpirationMillis;

    @Value("${tchuilem.jwt.issuer}")  // Injection de l'émetteur depuis application.properties
    private String tokenIssuer;

    // Générer un token JWT
    public String generateToken(UserDTO userDto) {
        // Récupère le rôle unique de l'utilisateur
        String userRole = userDto.getAuthorities().stream().map(Object::toString).findFirst().orElse("ROLE_USER");

        Map<String, Object> jsonPayload = new HashMap<>();
        jsonPayload.put("id", userDto.getId());
        jsonPayload.put("email", userDto.getEmail());
        jsonPayload.put("role", userDto.getRole());  // Met un seul rôle dans le payload

        return JWT.create()
                .withSubject(userDto.getEmail())
                .withIssuer(tokenIssuer)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + tokenExpirationMillis))
                .withPayload(jsonPayload)
                .sign(Algorithm.HMAC256(tokenSecretKey));
    }

    // Extraire le token JWT depuis l'en-tête Authorization
    public String extractToken(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ") ?
                authorizationHeader.substring(7) : null;
    }

    public String getToken(HttpServletRequest request) {
        String header = request.getHeader(tokenHeader);
        if (StringUtils.hasText(header) && header.startsWith(tokenPrefix)) {
            return header.replace(tokenPrefix, "").trim();
        }
        return null;
    }

    // Extraire l'utilisateur et ses rôles à partir du token
    public UserDetails extractUser(String jwtToken) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(tokenSecretKey))
                .withIssuer(tokenIssuer)
                .build()
                .verify(jwtToken);

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                // Récupère le rôle unique du token et le transforme en SimpleGrantedAuthority
                String role = decodedJWT.getClaim("role").asString();
                return Collections.singletonList(new SimpleGrantedAuthority(role));
            }

            @Override
            public String getPassword() {
                return null;  // Pas nécessaire pour l'authentification via JWT
            }

            @Override
            public String getUsername() {
                return decodedJWT.getSubject();  // L'email de l'utilisateur (nom d'utilisateur)
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;  // Pour la gestion des comptes expirés
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;  // Pour la gestion des comptes verrouillés
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;  // Pour la gestion de l'expiration des identifiants
            }

            @Override
            public boolean isEnabled() {
                return true;  // Pour la gestion de l'activation du compte
            }
        };
    }

    // Valider le token
    public boolean isTokenValid(String token, String username) {
        DecodedJWT decodedJWT = decodeToken(token);
        return (decodedJWT.getSubject().equals(username) && !isTokenExpired(decodedJWT));
    }

    // Décoder le token
    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(tokenSecretKey))
                .withIssuer(tokenIssuer)  // Vérification de l'émetteur
                .build()
                .verify(token);
    }

    public String getTokenPrefix() {
        return tokenPrefix;
    }

    public String getTokenHeader() {
        return tokenHeader;
    }


    // Vérifier si le token est expiré
    private boolean isTokenExpired(DecodedJWT decodedJWT) {
        return decodedJWT.getExpiresAt().before(new Date());
    }

    // Extraire le rôle de l'utilisateur depuis le token
    public String extractRole(String token) {
        DecodedJWT decodedJWT = decodeToken(token);
        return decodedJWT.getClaim("role").asString();
    }
}
