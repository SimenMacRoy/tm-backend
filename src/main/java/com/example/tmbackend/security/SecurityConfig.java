package com.example.tmbackend.security;

import com.example.tmbackend.config.AuthEntryPoint;
import com.example.tmbackend.config.AuthFilter;
import com.example.tmbackend.dto.CustomUserDetails;
import com.example.tmbackend.dto.UserDTO;
import com.example.tmbackend.exceptions.NotFoundException;
import com.example.tmbackend.model.User;
import com.example.tmbackend.repository.UserRepository;
import com.example.tmbackend.security.JwtAuthenticationFilter;
import com.example.tmbackend.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    private final AuthEntryPoint authEntryPoint;

    public SecurityConfig(UserRepository userRepository, JwtUtil jwtUtil, AuthEntryPoint authEntryPoint) {

        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authEntryPoint = authEntryPoint ;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(name = "userDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository.findByEmail(username).orElseThrow(
                    () -> new NotFoundException("Aucun utilisateur trouvé avec l'email " + username + ".")
            );
            // Utilise le constructeur pour convertir l'entité User en UserDTO qui implémente UserDetails
            return new CustomUserDetails(user);
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider()) //  Ceci est crucial
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exceptionHandlingConfigurer ->
                        exceptionHandlingConfigurer.authenticationEntryPoint(authEntryPoint)
                )
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequestsConfigurer ->
                        authorizeHttpRequestsConfigurer.requestMatchers("/task-master/**").permitAll()
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(new AuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
