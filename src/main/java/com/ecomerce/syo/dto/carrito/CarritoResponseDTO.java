package com.ecomerce.syo.dto.carrito;

import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Respuesta completa del carrito del cliente.
 * El frontend usa esto para mostrar el carrito y calcular el total.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarritoResponseDTO {
    private UUID idcarrito;
    private OffsetDateTime fechacreacion;
    private List<CarritoItemResponseDTO> items;
    private BigDecimal total; // suma de todos los subtotales
    private int cantidadProductos; // cantidad total de unidades en el carrito
}
