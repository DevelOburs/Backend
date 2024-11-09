package com.fridgify.shared.jwt.filter;

import com.fridgify.shared.jwt.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtRequestFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (
            authorizationHeader != null &&
            authorizationHeader.startsWith("Bearer ")
        ) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(jwt);
        }

        if (
            username != null &&
            SecurityContextHolder.getContext().getAuthentication() == null
        ) {
            // Normally, you'd load the user details here and set the authentication
            if (jwtUtil.validateToken(jwt, username)) {
                // Create an Authentication object and set it in the context
                UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        new ArrayList<>()
                    );
                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        chain.doFilter(request, response);
    }
}
