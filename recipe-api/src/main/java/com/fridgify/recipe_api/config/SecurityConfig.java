package com.fridgify.recipe_api.config;

import com.fridgify.shared.jwt.filter.JwtRequestFilter;
import com.fridgify.shared.jwt.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) -> auth
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html",
                            "/actuator/**"
                    ).permitAll()
                    .anyRequest().permitAll()
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/**"))
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        JwtUtil jwtUtil = new JwtUtil();
        return new JwtRequestFilter(jwtUtil);
    }
}