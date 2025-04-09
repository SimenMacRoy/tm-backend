package com.example.tmbackend.security;

import com.example.tmbackend.dto.CustomUserDetails;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.UserRepository;
import com.example.tmbackend.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Extraire le token de l'en-tête Authorization
        String token = jwtUtil.extractToken(request.getHeader("Authorization"));
        System.out.println("Extracted token: " + token);

        // Si le token est valide, définir l'authentification dans le contexte
        if (token != null && jwtUtil.isTokenValid(token, request.getUserPrincipal().getName())) {
            // Extraire l'email de l'utilisateur depuis le token
            String username = jwtUtil.decodeToken(token).getSubject();  // L'email de l'utilisateur

            // Récupérer l'utilisateur à partir de la base de données
            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new RuntimeException("Utilisateur non trouvé")
            );

            // Créer un CustomUserDetails à partir de l'objet User
            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            // Créer un token d'authentification avec les autorités (rôles) de l'utilisateur
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails, null, List.of(new SimpleGrantedAuthority(user.getRole())));

            // Ajouter l'authentification dans le contexte de sécurité de Spring
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Passer à la prochaine étape du filtre dans la chaîne
        filterChain.doFilter(request, response);
    }
}
