package com.ecomerce.syo.controller;

import com.ecomerce.syo.dto.clientes.AuthResponseDTO;
import com.ecomerce.syo.dto.clientes.LoguinClienteDTO;
import com.ecomerce.syo.dto.clientes.PerfilClienteDTO;
import com.ecomerce.syo.dto.clientes.RegistroClienteDTO;
import com.ecomerce.syo.dto.clientes.UpdateClienteDTO;
import com.ecomerce.syo.model.Cliente;
import com.ecomerce.syo.security.JwtUtil;
import com.ecomerce.syo.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final JwtUtil jwtUtil;
    private final com.ecomerce.syo.repository.ClienteRepository clienteRepository;  // ← Agregado

    // ====================== RUTAS PÚBLICAS ======================
    @PostMapping("/registro")
    public ResponseEntity<Cliente> registrar(@RequestBody RegistroClienteDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clienteService.registrar(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoguinClienteDTO dto) {
        Cliente cliente = clienteService.login(dto);

        String token = jwtUtil.generateToken(cliente.getCorreo(), 
                                             cliente.getIdcliente().getMostSignificantBits());

        AuthResponseDTO response = AuthResponseDTO.builder()
                .token(token)
                .idcliente(cliente.getIdcliente())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .correo(cliente.getCorreo())
                .build();

        return ResponseEntity.ok(response);
    }

    // ====================== RUTAS PROTEGIDAS ======================
    @GetMapping("/{id}")
    public ResponseEntity<PerfilClienteDTO> obtenerPerfil(@PathVariable UUID id) {
        String correoLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getCorreo().equals(correoLogueado)) {
            throw new RuntimeException("No tienes permiso para ver este perfil");
        }

        return ResponseEntity.ok(clienteService.obtenerPerfil(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizarPerfil(@PathVariable UUID id, 
                                                    @RequestBody UpdateClienteDTO dto) {
        String correoLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getCorreo().equals(correoLogueado)) {
            throw new RuntimeException("No tienes permiso para modificar este perfil");
        }

        return ResponseEntity.ok(clienteService.actualizarPerfil(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCuenta(@PathVariable UUID id) {
        String correoLogueado = SecurityContextHolder.getContext().getAuthentication().getName();

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (!cliente.getCorreo().equals(correoLogueado)) {
            throw new RuntimeException("No tienes permiso para eliminar este perfil");
        }

        clienteService.eliminarCuenta(id);
        return ResponseEntity.noContent().build();
    }
}