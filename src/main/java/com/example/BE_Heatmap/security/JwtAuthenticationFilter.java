package com.example.BE_Heatmap.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    // Constructor to inject JwtUtil dependency
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws jakarta.servlet.ServletException, java.io.IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String userId = null;

        // Extract the JWT token from the Authorization header if it's in the format "Bearer <token>"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);  // Extract token after "Bearer "
            userId = jwtUtil.extractUserId(token);  // Extract userId from the token
        }

        // Validate the token and set the authentication if valid
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (jwtUtil.validateToken(token)) {
                // If the token is valid, create an authentication object with the userId
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, null);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                System.out.println("JWT Token validated and user authenticated: " + userId);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        chain.doFilter(request, response);
    }
}
