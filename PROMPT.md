# PROMPT COMPLETO — Proyecto SYO Ecommerce
# Backend: Spring Boot + Spring Security (JWT) + Neon PostgreSQL
# Fecha de corte: proyecto tal como está en el zip actual

---

## STACK TÉCNICO

- Java + Spring Boot
- Spring Security con JWT (JJWT 0.12.6)
- Spring Data JPA + Hibernate
- PostgreSQL en Neon (ddl-auto=validate — la BD ya existe, Hibernate NO crea tablas)
- Lombok
- hypersistence-utils (para columna JSONB en Producto)
- Despliegue backend en Render: https://syo-orb7.onrender.com
- Frontend en Vite+React: http://localhost:5173

---

## CONEXIÓN BASE DE DATOS

```
URL:      jdbc:postgresql://ep-still-snow-amp53gw4-pooler.c-5.us-east-1.aws.neon.tech/neondb?sslmode=require
Username: neondb_owner
Password: npg_An5POCKz3UaV
```

---

## ESQUEMA DE BASE DE DATOS

### categorias
```sql
idcategoria uuid PK DEFAULT gen_random_uuid()
nombre      varchar(100) NOT NULL
descripcion text
imgurl      text
```

### clientes
```sql
idcliente    uuid PK DEFAULT gen_random_uuid()
nombre       varchar(100) NOT NULL
apellido     varchar(100) NOT NULL
dni          varchar(8)   NOT NULL UNIQUE
numero       varchar(15)  NOT NULL UNIQUE
direccion    text         NOT NULL
referencia   text
distrito     varchar(100)
codigopostal varchar(10)
correo       varchar(255) NOT NULL UNIQUE
contrasena   text         NOT NULL
```

### estadospedido
```sql
idestado uuid PK DEFAULT gen_random_uuid()
nombre   varchar(50) NOT NULL UNIQUE
```
Datos seeded (IDs reales en BD):
| UUID | Nombre |
|---|---|
| 04d0c5fa-3046-4ee9-8832-f653337e2ac0 | Rntregado   ← typo real, NO corregir |
| 4d0c4791-544e-4976-aa99-ba17dee51a4b | Enviado |
| 7fbfaff7-254c-47f7-a7b5-0816608e9146 | Cancelado |
| 81d769fe-5ed5-4b5c-95ca-5e72a7a13537 | Pendiente |
| 846815ff-5963-42a4-a29e-21cb65cd9c31 | En preparacion |
| 8807bfc7-19d1-405a-b853-efbfc5a9a7be | Pagado |

### productos
```sql
idproducto               uuid PK DEFAULT gen_random_uuid()
categoriaid              uuid NOT NULL FK→categorias
nombre                   varchar(200) NOT NULL
marca                    varchar(100) NOT NULL
precio                   numeric(10,2) NOT NULL
stock                    integer NOT NULL DEFAULT 0
descuento                numeric(5,2) NOT NULL DEFAULT 0
descripcion              text
especificacionestecnicas jsonb
imgurl                   text
preciodesct              numeric(10,2) GENERATED ALWAYS AS (precio*(1-descuento/100.0)) STORED
```
IMPORTANTE: preciodesct es columna GENERATED en BD → en JPA no se mapea como insertable/updatable, solo se lee.

### pedidos
```sql
idpedido        uuid PK DEFAULT gen_random_uuid()
clienteid       uuid NOT NULL FK→clientes
estadoid        uuid NOT NULL FK→estadospedido
fecha           timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
tipoentrega     varchar(10) NOT NULL   -- "tienda" | "delivery"
tipocomprobante varchar(10) NOT NULL   -- "boleta" | "factura"
total           numeric(12,2) NOT NULL
```

### detallepedidos
```sql
iddetalle      uuid PK DEFAULT gen_random_uuid()
pedidoid       uuid NOT NULL FK→pedidos (CASCADE DELETE)
productoid     uuid NOT NULL FK→productos (RESTRICT DELETE)
cantidad       integer NOT NULL
preciounitario numeric(10,2) NOT NULL   -- snapshot del precio al momento de compra
descuento      numeric(5,2) NOT NULL DEFAULT 0  -- snapshot del descuento al momento de compra
preciototal    numeric(12,2) NOT NULL   -- preciounitario * cantidad * (1 - descuento/100)
```

### historialestadospedido
```sql
idhistorial uuid PK DEFAULT gen_random_uuid()
pedidoid    uuid NOT NULL FK→pedidos (CASCADE DELETE)
estadoid    uuid NOT NULL FK→estadospedido (RESTRICT DELETE)
fecha       timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
```

### pagos
```sql
idpago      uuid PK DEFAULT gen_random_uuid()
pedidoid    uuid NOT NULL FK→pedidos (RESTRICT DELETE)
fechapago   timestamptz NOT NULL DEFAULT CURRENT_TIMESTAMP
estadopago  varchar(20) NOT NULL   -- "pendiente" | "completado" | "rechazado"
metodopago  varchar(30) NOT NULL   -- "yape" | "tarjeta"
```

---

## ESTRUCTURA DE PAQUETES

```
com.ecomerce.syo/
├── SyoApplication.java
├── config/
│   └── SecurityConfig.java
├── controller/
│   ├── CategoriaController.java
│   ├── ClienteController.java
│   ├── PedidoController.java
│   └── ProductoController.java
├── dto/
│   ├── categoria/
│   │   ├── CategoriaDetalleDTO.java
│   │   └── CategoriaListaDTO.java
│   ├── clientes/
│   │   ├── AuthResponseDTO.java
│   │   ├── LoguinClienteDTO.java
│   │   ├── PerfilClienteDTO.java
│   │   ├── RegistroClienteDTO.java
│   │   └── UpdateClienteDTO.java
│   ├── pedido/
│   │   ├── ActualizarCantidadDTO.java
│   │   ├── CrearPedidoDTO.java
│   │   ├── ItemCarritoDTO.java
│   │   ├── PedidoDetalleDTO.java       ← contiene sub-DTOs: DetalleItemDTO, HistorialEstadoDTO, PagoDTO
│   │   └── PedidoResumenDTO.java
│   └── producto/
│       ├── ProductoDetalleDTO.java
│       └── ProductoListaDTO.java
├── execption/
│   └── ResourceNotFoundException.java
├── model/
│   ├── Categoria.java
│   ├── Cliente.java
│   ├── DetallePedido.java
│   ├── EstadoPedido.java
│   ├── HistorialEstadoPedido.java
│   ├── Pago.java
│   ├── Pedido.java
│   └── Producto.java
├── repository/
│   ├── CategoriaRepository.java
│   ├── ClienteRepository.java
│   ├── DetallePedidoRepository.java
│   ├── EstadoPedidoRepository.java
│   ├── HistorialEstadoPedidoRepository.java
│   ├── PagoRepository.java
│   ├── PedidoRepository.java
│   └── ProductoRepository.java
├── security/
│   ├── JwtRequestFilter.java
│   └── JwtUtil.java
└── services/
    ├── CategoriaService.java
    ├── ClienteService.java
    ├── PedidoService.java
    ├── ProductoService.java
    └── impl/
        ├── CategoriaServiceImpl.java
        ├── ClienteServiceImpl.java
        ├── PedidoServiceImpl.java
        └── ProductoServiceImpl.java
```

---

## SEGURIDAD — SecurityConfig.java

JWT stateless. Filtro: JwtRequestFilter (lee "Authorization: Bearer xxx").

RUTAS PÚBLICAS (sin token):
- GET  /api/productos/**
- GET  /api/categorias/**
- POST /api/clientes/registro
- POST /api/clientes/login

RUTAS PROTEGIDAS (JWT requerido):
- Cualquier otra ruta bajo /api/**

CORS permitido desde:
- http://localhost:5173
- https://syo-orb7.onrender.com

---

## JWT — JwtUtil.java

- Algoritmo: HS512
- SECRET_KEY: generada en runtime con Keys.secretKeyFor (se pierde al reiniciar — tokens no persisten entre reinicios)
- Expiración: 24 horas
- Subject: correo del cliente
- Claim extra: "id" (long: getMostSignificantBits del UUID del cliente)
- extractCorreo(token) → devuelve el correo para validar propietario en cada operación

---

## ENDPOINTS COMPLETOS

### CATEGORÍAS (público)
```
GET  /api/categorias          → List<CategoriaListaDTO>       (id, nombre, imgurl)
GET  /api/categorias/{id}     → CategoriaDetalleDTO           (id, nombre, descripcion, imgurl)
```

### PRODUCTOS (público)
```
GET  /api/productos                                         → Page<ProductoListaDTO>
     ?categoriaId=uuid&precioMin=x&precioMax=y&page=0&size=20&sort=nombre
GET  /api/productos/detalle/{id}                           → ProductoDetalleDTO
GET  /api/productos/categoria/{id}                         → Page<ProductoListaDTO>
```

### CLIENTES
```
POST /api/clientes/registro   → Cliente           (público)
POST /api/clientes/login      → AuthResponseDTO   (público) → devuelve token JWT
GET  /api/clientes/{id}       → PerfilClienteDTO  (protegido, solo el dueño)
PUT  /api/clientes/{id}       → Cliente           (protegido, solo el dueño)
DELETE /api/clientes/{id}     → 204               (protegido, solo el dueño)
```

### PEDIDOS (todos protegidos, solo el cliente dueño)
```
POST   /api/pedidos                                → PedidoResumenDTO   (crear pedido)
POST   /api/pedidos/{id}/pagar?metodopago=yape     → PedidoDetalleDTO   (procesar pago)
GET    /api/pedidos/cliente/{clienteid}            → List<PedidoResumenDTO>  (todos mis pedidos)
GET    /api/pedidos/cliente/{clienteid}/carrito    → List<PedidoResumenDTO>  (solo Pendiente)
GET    /api/pedidos/cliente/{clienteid}/estado?nombre=Enviado → List<PedidoResumenDTO>
GET    /api/pedidos/{id}                           → PedidoDetalleDTO   (detalle + historial + pago)
PUT    /api/pedidos/{id}/detalle/{detalleid}       → PedidoDetalleDTO   (cambiar cantidad ítem)
DELETE /api/pedidos/{id}                           → 204                (solo Pendiente)
```

---

## LÓGICA DE NEGOCIO CLAVE

### Registro de cliente
- Validaciones de duplicado: correo, dni, número
- Contraseña: mínimo 8 chars, 1 mayúscula, 1 número, sin 3+ dígitos consecutivos iguales
- Contraseña guardada con BCrypt

### Crear pedido (POST /api/pedidos)
Body:
```json
{
  "clienteid": "uuid",
  "tipoentrega": "tienda|delivery",
  "tipocomprobante": "boleta|factura",
  "metodopago": "yape|tarjeta",
  "items": [
    { "productoid": "uuid", "cantidad": 2 }
  ]
}
```
Proceso:
1. Valida JWT coincide con clienteid
2. Verifica stock por cada producto
3. Crea Pedido en estado "Pendiente"
4. Crea DetallePedido por cada item (snapshot de precio y descuento)
5. preciototal = preciounitario × cantidad × (1 - descuento/100)
6. Descuenta stock de cada producto
7. Actualiza total del Pedido
8. Inserta en historialestadospedido → estado "Pendiente"

### Procesar pago (POST /api/pedidos/{id}/pagar)
Proceso:
1. Valida que el pedido está en "Pendiente"
2. Crea registro en pagos (estadopago: "completado", metodopago: param)
3. Cambia pedido → "Pagado" + inserta historial
4. Cambia pedido → "En preparacion" + inserta historial
Todo en una sola @Transactional

### Actualizar cantidad (PUT /api/pedidos/{id}/detalle/{detalleid})
- Solo si pedido está en "Pendiente"
- Ajusta stock del producto (diferencia entre nueva y anterior cantidad)
- Recalcula preciototal del detalle
- Recalcula total del pedido sumando todos los detalles

### Eliminar pedido (DELETE /api/pedidos/{id})
- Solo si pedido está en "Pendiente"
- Devuelve stock de todos los productos del pedido
- CASCADE elimina detalles e historial

---

## FLUJO DE ESTADOS DEL PEDIDO

```
Pendiente
    ↓ (al pagar)
  Pagado          ← se registra en historialestadospedido
    ↓ (inmediato tras pago)
  En preparacion  ← se registra en historialestadospedido
    ↓ (tienda actualiza manualmente en el futuro)
  Enviado
    ↓
  Rntregado  ← typo en BD, no corregir
  
  Cualquier estado → Cancelado (no implementado aún en el código)
```

Cada cambio de estado = nueva fila en historialestadospedido con fecha automática.
El cliente puede ver el timeline completo con GET /api/pedidos/{id}.

---

## MODELOS JPA IMPORTANTES

### Producto — campo GENERATED
```java
// preciodesct es GENERATED ALWAYS en PostgreSQL
// JPA solo lo lee, nunca lo escribe
@Column(name = "preciodesct", nullable = false, precision = 10, scale = 2)
private BigDecimal preciodesct;
// Si Hibernate intenta insertar este campo → error en BD
// Solución si hay errores: agregar insertable=false, updatable=false
```

### Pedido — relaciones
```java
@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
private List<DetallePedido> detalles;

@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
private List<HistorialEstadoPedido> historial;

@OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
private List<Pago> pagos;
```

---

## REPOSITORIES — MÉTODOS CUSTOM

```java
// ClienteRepository
findByCorreo(String correo)
existsByCorreo / existsByDni / existsByNumero

// ProductoRepository
findByIdWithCategoria(UUID id)              // JOIN FETCH para evitar LazyInit
findByCategoria_Idcategoria(UUID, Pageable)
buscarConFiltros(UUID cat, BigDecimal min, BigDecimal max, Pageable)

// PedidoRepository
findAllByCliente_IdclienteOrderByFechaDesc(UUID clienteid)
findByClienteAndEstado(UUID clienteid, String estado)   // @Query JPQL

// DetallePedidoRepository
findAllByPedido_Idpedido(UUID pedidoid)

// HistorialEstadoPedidoRepository
findAllByPedido_IdpedidoOrderByFechaAsc(UUID pedidoid)

// EstadoPedidoRepository
findByNombre(String nombre)    // para obtener UUID del estado por nombre

// PagoRepository
findByPedido_Idpedido(UUID pedidoid)    // Optional → puede no existir si pedido es Pendiente
```

---

## DTOs DE RESPUESTA

### AuthResponseDTO (login exitoso)
```json
{
  "token": "eyJ...",
  "tipo": "Bearer",
  "idcliente": "uuid",
  "nombre": "Juan",
  "apellido": "Pérez",
  "correo": "juan@email.com"
}
```

### PedidoResumenDTO (lista de pedidos)
```json
{
  "idpedido": "uuid",
  "fecha": "2025-01-15T10:30:00Z",
  "estado": "En preparacion",
  "tipoentrega": "delivery",
  "tipocomprobante": "boleta",
  "total": 299.90,
  "cantidadItems": 3
}
```

### PedidoDetalleDTO (detalle completo)
```json
{
  "idpedido": "uuid",
  "fecha": "2025-01-15T10:30:00Z",
  "estadoActual": "En preparacion",
  "tipoentrega": "delivery",
  "tipocomprobante": "boleta",
  "total": 299.90,
  "items": [
    {
      "iddetalle": "uuid",
      "productoid": "uuid",
      "nombreProducto": "Monitor LG 27\"",
      "imgurl": "https://...",
      "cantidad": 1,
      "preciounitario": 299.90,
      "descuento": 0.00,
      "preciototal": 299.90
    }
  ],
  "historial": [
    { "estado": "Pendiente",       "fecha": "2025-01-15T10:30:00Z" },
    { "estado": "Pagado",          "fecha": "2025-01-15T10:31:00Z" },
    { "estado": "En preparacion",  "fecha": "2025-01-15T10:31:00Z" }
  ],
  "pago": {
    "idpago": "uuid",
    "fechapago": "2025-01-15T10:31:00Z",
    "estadopago": "completado",
    "metodopago": "yape"
  }
}
```

---

## NOTAS Y ADVERTENCIAS

1. SECRET_KEY del JWT se regenera cada reinicio del servidor → todos los tokens quedan inválidos al reiniciar. Para producción real usar una clave fija en application.properties.

2. El typo "Rntregado" existe en la tabla estadospedido. El código lo referencia exactamente así. NO corregir sin hacer UPDATE en la BD primero.

3. ddl-auto=validate → Hibernate valida que las entidades coincidan con la BD pero NO crea ni altera tablas. Si se agrega un campo nuevo en una entidad sin el ALTER TABLE correspondiente en Neon, la app no arranca.

4. preciodesct en Producto es GENERATED ALWAYS → si Hibernate lanza error al insertar/actualizar, agregar insertable=false, updatable=false a la anotación @Column de ese campo.

5. El controller de clientes inyecta ClienteRepository directamente (además del service) para validar propietario — esto es funcional pero podría moverse al service.

6. No hay manejo global de excepciones (@ControllerAdvice / @ExceptionHandler). Los RuntimeException llegan al cliente como 500. Se puede agregar un GlobalExceptionHandler para retornar 404/403/400 correctamente.