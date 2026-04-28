package com.ecomerce.syo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configuración de Seguridad + CORS para desarrollo
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // ==================== CORS ====================
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // ==================== Desactivar CSRF (desarrollo) ====================
                .csrf(csrf -> csrf.disable())

                // ==================== Autorización ====================
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**").permitAll() // Permite todas las rutas de tu API
                        .anyRequest().permitAll())

                // Desactivar login básico y formulario
                .httpBasic(httpBasic -> httpBasic.disable())
                .formLogin(form -> form.disable());

        return http.build();
    }

    /**
     * Configuración de CORS
     * Permite que tu frontend en puerto 5173 (Vite) pueda llamar al backend
     */
 @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    // Agregamos la URL de Render junto con la de Localhost
    configuration.setAllowedOrigins(List.of(
        "http://localhost:5173",          // Para cuando pruebas en tu PC
        "https://syo-orb7.onrender.com"   // Tu frontend ya desplegado
    ));

    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
}
}