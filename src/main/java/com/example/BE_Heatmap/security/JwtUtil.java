package com.example.BE_Heatmap.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {

    private final Algorithm algorithm = Algorithm.HMAC256("Tr@velHe@tM@p");
    private final String issuer = "travelHeatMapApp";
    private final JWTVerifier verifier;

    public JwtUtil() {
        verifier = JWT.require(algorithm)
                .withIssuer(issuer)
                .build();
    }

    // Generate JWT Token
    public String generateToken(String userId) {
        return JWT.create()
                .withIssuer(issuer)
                .withSubject("user details")
                .withClaim("userId", userId)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() +  3600000 * 8))
                .withJWTId(UUID.randomUUID()
                        .toString())
                .sign(algorithm);
    }

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            // DecodedJWT decodedJWT = verifier.verify(token);
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    // Extract username from the token
    public String extractUserId(String token) {
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaim("userId").toString(); // Get the subject (username)
    }
}
