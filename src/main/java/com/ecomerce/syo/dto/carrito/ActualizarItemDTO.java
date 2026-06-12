package com.ecomerce.syo.dto.carrito;

import lombok.*;

/**
 * Body para cambiar la cantidad de un item del carrito.
 * PUT /api/carrito/item/{itemid}
 * Si cantidad = 0 → el item se elimina automáticamente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarItemDTO {
    private Integer cantidad;
}
