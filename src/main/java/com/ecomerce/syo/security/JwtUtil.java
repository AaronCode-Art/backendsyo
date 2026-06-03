package com.ecomerce.syo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JwtUtil - Utilidad para generar y validar tokens JWT
 * Versión compatible con JJWT 0.12.6
 */
@Component
public class JwtUtil {

    // Clave secreta fuerte para firmar los tokens
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    // Duración del token: 24 horas
    private static final long EXPIRATION_TIME = 86400000;

    /**
     * Genera un token JWT con el correo y el ID del cliente
     */
    public String generateToken(String correo, Long clienteId) {
        return Jwts.builder()
                .subject(correo)
                .claim("id", clienteId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    /**
     * Extrae el correo del token JWT
     */
    public String extractCorreo(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /**
     * Valida si el token es válido y no ha expirado
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}