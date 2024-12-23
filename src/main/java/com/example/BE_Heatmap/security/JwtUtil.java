package com.example.BE_Heatmap.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    private final String secretKey = System.getenv("JWT_SECRET_KEY");

    // Generate JWT Token
    public String generateToken(String username) {
        return JWT.create()
                .withSubject(username) // Set the username as the subject
                .withIssuedAt(new Date()) // Set issued date
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1-hour expiry
                .sign(Algorithm.HMAC256(secretKey)); // Sign with HMAC256 and secret key
    }

    // Validate JWT Token
    public boolean validateToken(String token, String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withSubject(username)
                    .build();

            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Extract username from the token
    public String extractUsername(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getSubject(); // Get the subject (username)
    }
}
