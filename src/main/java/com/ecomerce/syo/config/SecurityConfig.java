package com.ecomerce.syo.config;

import com.ecomerce.syo.security.JwtRequestFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * SecurityConfig - Configuración de seguridad con JWT stateless.
 *
 * RUTAS PÚBLICAS (sin token):
 *   GET  /api/productos/**
 *   GET  /api/categorias/**
 *   POST /api/clientes/registro
 *   POST /api/clientes/login
 *
 * RUTAS PROTEGIDAS (requieren JWT → Authorization: Bearer <token>):
 *   /api/clientes/{id}   → perfil del cliente
 *   /api/carrito/**      → carrito de compras
 *   /api/pedidos/**      → pedidos
 */
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // ── Públicas ──────────────────────────────────────────────────
                .requestMatchers(HttpMethod.GET,  "/api/productos/**").permitAll()
                .requestMatchers(HttpMethod.GET,  "/api/categorias/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes/registro").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes/login").permitAll()
                // ── Todo lo demás requiere JWT ─────────────────────────────────
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173", "https://syo-orb7.onrender.com", "https://aaroncode-art.github.io/SyO/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
