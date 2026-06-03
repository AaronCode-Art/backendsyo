package com.ecomerce.syo.dto.carrito;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoResponseDTO {
    private UUID idcarrito;
    private LocalDateTime fechacreacion;
    private List<CarritoItemDTO> items;
    private BigDecimal total;

}