package com.ecomerce.syo.dto.carrito;

import lombok.*;
import java.util.UUID;

/**
 * Body para agregar o actualizar un producto en el carrito.
 * POST /api/carrito/agregar
 *
 * Si el producto ya existe en el carrito → se suma la cantidad.
 * Si no existe → se crea el item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgregarCarritoDTO {
    private UUID productoid;
    private Integer cantidad; // mínimo 1
}
