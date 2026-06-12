package com.ecomerce.syo.model;

import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "idproducto", updatable = false, nullable = false)
    private UUID idproducto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoriaid", nullable = false)
    private Categoria categoria;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "marca", nullable = false, length = 100)
    private String marca;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "preciodesct", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal preciodesct;
    
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "descuento", precision = 5, scale = 2)
    private BigDecimal descuento;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Type(JsonBinaryType.class)
    @Column(name = "especificacionestecnicas", columnDefinition = "jsonb")
    private Map<String, Object> especificacionestecnicas;

    @Column(name = "imgurl", columnDefinition = "TEXT")
    private String imgurl;

}
