package com.ecomerce.syo.services.impl;

import com.ecomerce.syo.dto.clientes.LoguinClienteDTO;
import com.ecomerce.syo.dto.clientes.PerfilClienteDTO;
import com.ecomerce.syo.dto.clientes.RegistroClienteDTO;
import com.ecomerce.syo.dto.clientes.UpdateClienteDTO;
import com.ecomerce.syo.model.Cliente;
import com.ecomerce.syo.repository.ClienteRepository;
import com.ecomerce.syo.services.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Cliente registrar(RegistroClienteDTO dto) {

        // Validaciones de duplicados
        if (clienteRepository.existsByCorreo(dto.getCorreo())) {
            throw new RuntimeException("El correo ya está registrado");
        }
        if (clienteRepository.existsByDni(dto.getDni())) {
            throw new RuntimeException("El DNI ya está registrado");
        }
        if (clienteRepository.existsByNumero(dto.getNumero())) {
            throw new RuntimeException("El número de teléfono ya está registrado");
        }

        // Validar contraseña
        if (!esPasswordValida(dto.getContrasena())) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres, al menos una mayúscula, un número y no números consecutivos.");
        }

        // Crear cliente usando setters (más seguro que builder en algunos casos)
        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setApellido(dto.getApellido());
        cliente.setDni(dto.getDni());
        cliente.setNumero(dto.getNumero());
        cliente.setDireccion(dto.getDireccion());
        cliente.setReferencia(dto.getReferencia());
        cliente.setDistrito(dto.getDistrito());
        cliente.setCodigopostal(dto.getCodigopostal());
        cliente.setCorreo(dto.getCorreo());
        cliente.setContrasena(passwordEncoder.encode(dto.getContrasena()));

        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente login(LoguinClienteDTO dto) {
        Cliente cliente = clienteRepository.findByCorreo(dto.getCorreo())
                .orElseThrow(() -> new RuntimeException("Correo o contraseña incorrectos"));

        if (!passwordEncoder.matches(dto.getContrasena(), cliente.getContrasena())) {
            throw new RuntimeException("Correo o contraseña incorrectos");
        }

        return cliente;
    }

    @Override
    public PerfilClienteDTO obtenerPerfil(UUID idcliente) {
        Cliente cliente = clienteRepository.findById(idcliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        return PerfilClienteDTO.builder()
                .idcliente(cliente.getIdcliente())
                .nombre(cliente.getNombre())
                .apellido(cliente.getApellido())
                .dni(cliente.getDni())
                .numero(cliente.getNumero())
                .direccion(cliente.getDireccion())
                .referencia(cliente.getReferencia())
                .distrito(cliente.getDistrito())
                .codigopostal(cliente.getCodigopostal())
                .correo(cliente.getCorreo())
                .build();
    }

    @Override
    public Cliente actualizarPerfil(UUID idcliente, UpdateClienteDTO dto) {
        Cliente cliente = clienteRepository.findById(idcliente)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (dto.getNombre() != null) cliente.setNombre(dto.getNombre());
        if (dto.getApellido() != null) cliente.setApellido(dto.getApellido());
        if (dto.getNumero() != null) cliente.setNumero(dto.getNumero());
        if (dto.getDireccion() != null) cliente.setDireccion(dto.getDireccion());
        if (dto.getReferencia() != null) cliente.setReferencia(dto.getReferencia());
        if (dto.getDistrito() != null) cliente.setDistrito(dto.getDistrito());
        if (dto.getCodigopostal() != null) cliente.setCodigopostal(dto.getCodigopostal());
        if (dto.getCorreo() != null) cliente.setCorreo(dto.getCorreo());

        if (dto.getContrasena() != null && !dto.getContrasena().isBlank()) {
            if (!esPasswordValida(dto.getContrasena())) {
                throw new RuntimeException("La nueva contraseña no cumple con las reglas");
            }
            cliente.setContrasena(passwordEncoder.encode(dto.getContrasena()));
        }

        return clienteRepository.save(cliente);
    }

    @Override
    public void eliminarCuenta(UUID idcliente) {
        if (!clienteRepository.existsById(idcliente)) {
            throw new RuntimeException("Cliente no encontrado");
        }
        clienteRepository.deleteById(idcliente);
    }

    private boolean esPasswordValida(String password) {
        if (password == null || password.length() < 8) return false;

        boolean tieneMayuscula = password.chars().anyMatch(Character::isUpperCase);
        boolean tieneNumero = password.chars().anyMatch(Character::isDigit);

        if (!tieneMayuscula || !tieneNumero) return false;

        return !password.matches(".*(\\d)\\1{2,}.*");
    }
}