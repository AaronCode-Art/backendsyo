package com.ecomerce.syo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JwtRequestFilter
 * 
 * Este filtro se ejecuta en CADA petición HTTP.
 * Lee el token del header "Authorization: Bearer xxx"
 * y si es válido, lo pone en el contexto de Spring Security.
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Verificar si viene el token con formato "Bearer xxx"
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Quita "Bearer "

            try {
                // Validamos el token
                if (jwtUtil.validateToken(token)) {
                    String correo = jwtUtil.extractCorreo(token);

                    // Guardamos la autenticación en el contexto de Spring
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(correo, null, null);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                // Si el token es inválido, simplemente continuamos sin autenticación
                System.out.println("Token inválido: " + e.getMessage());
            }
        }

        // Continuar con el siguiente filtro
        chain.doFilter(request, response);
    }
}