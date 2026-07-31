# Módulo de Reportes

Nuevo `ReporteController` con dos endpoints de consulta consolidada, usando
**Specification JPA** (Criteria API) para combinar filtros opcionales de
forma dinámica en `MovimientoSpecification` y `AuditoriaSpecification`.

Ambos endpoints requieren estar autenticado (header `Authorization: Bearer <token>`).
`/reportes/auditoria` además requiere rol `ROLE_ADMIN`.

## Notas de diseño importantes

1. **`tipoMovimiento`**: el enum real del proyecto es `ENTRADA, SALIDA,
   TRANSFERENCIA` (no existe `AJUSTE` en el modelo actual). El endpoint
   valida contra estos 3 valores. Si necesitas `AJUSTE` como tipo real de
   negocio, dímelo y agrego el valor al enum `TipoMovimiento` (implica
   también ajustar la lógica de `MovimientoService.registrarMovimiento`
   para decidir si afecta el stock hacia arriba o hacia abajo).

2. **`producto` y `campoModificado` en auditoría**: la entidad `Auditoria`
   no guarda una referencia estructurada al producto ni un campo separado
   para "qué campo cambió" — solo guarda texto libre en `entidadAfectada`,
   `valoresAnteriores` y `valoresNuevos` (ej: `"Stock: 15"` → `"Stock: 20"`).
   Por eso estos dos filtros hacen una búsqueda de texto (`LIKE`,
   insensible a mayúsculas) dentro de esos campos, en vez de una
   comparación exacta contra una columna. Si más adelante quieres
   auditoría más precisa (ej. guardar el `productoId` y el nombre exacto
   del campo modificado), lo ideal sería ampliar la entidad `Auditoria` —
   puedo hacerlo si lo necesitas.

## GET /api/reportes/movimientos

| Parámetro       | Tipo               | Obligatorio | Descripción                                  |
|-----------------|--------------------|-------------|-----------------------------------------------|
| `bodegaId`      | Long                | No          | Filtra por bodega de origen O destino         |
| `productoId`    | Long                | No          | Filtra por producto                           |
| `tipoMovimiento`| ENTRADA/SALIDA/TRANSFERENCIA | No | Filtra por tipo de movimiento         |
| `fechaInicio`   | ISO datetime        | No          | `2026-07-01T00:00:00`                         |
| `fechaFin`      | ISO datetime        | No          | `2026-07-31T23:59:59`                         |

### Ejemplos (Postman: método GET, header `Authorization: Bearer <token>`)

**Todos los movimientos (sin filtros):**
```
GET http://localhost:8081/api/reportes/movimientos
```

**Movimientos de tipo SALIDA entre dos fechas:**
```
GET http://localhost:8081/api/reportes/movimientos?tipoMovimiento=SALIDA&fechaInicio=2026-07-01T00:00:00&fechaFin=2026-07-31T23:59:59
```

**Movimientos de un producto específico:**
```
GET http://localhost:8081/api/reportes/movimientos?productoId=3
```

**Combinando bodega + tipo + rango de fechas:**
```
GET http://localhost:8081/api/reportes/movimientos?bodegaId=1&tipoMovimiento=ENTRADA&fechaInicio=2026-07-01T00:00:00&fechaFin=2026-07-31T23:59:59
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 12,
    "fecha": "2026-07-15T10:30:00",
    "tipoMovimiento": "SALIDA",
    "productoId": 3,
    "productoNombre": "Monitor 27 Pulgadas 4K",
    "cantidad": 2,
    "bodegaOrigenId": 1,
    "bodegaOrigenNombre": "Bodega Principal",
    "bodegaDestinoId": null,
    "bodegaDestinoNombre": null,
    "usuarioResponsable": "admin"
  }
]
```

## GET /api/reportes/auditoria (solo ADMIN)

| Parámetro         | Tipo         | Obligatorio | Descripción                                          |
|--------------------|--------------|-------------|-------------------------------------------------------|
| `producto`         | String       | No          | Busca coincidencia de texto en entidad/valores        |
| `campoModificado`  | String       | No          | Busca coincidencia de texto en los valores registrados|
| `fechaInicio`      | ISO datetime | No          | Rango de `fechaHora`                                   |
| `fechaFin`         | ISO datetime | No          | Rango de `fechaHora`                                   |

### Ejemplos

**Auditorías relacionadas con "Movimiento" (texto en entidadAfectada):**
```
GET http://localhost:8081/api/reportes/auditoria?producto=Movimiento
```

**Auditorías donde cambió el "Stock":**
```
GET http://localhost:8081/api/reportes/auditoria?campoModificado=Stock
```

**Rango de fechas:**
```
GET http://localhost:8081/api/reportes/auditoria?fechaInicio=2026-07-01T00:00:00&fechaFin=2026-07-31T23:59:59
```

**Respuesta de ejemplo:**
```json
[
  {
    "id": 8,
    "tipoOperacion": "INSERT",
    "fechaHora": "2026-07-15T10:30:00",
    "usuario": "admin",
    "entidadAfectada": "Movimiento",
    "valoresAnteriores": "Stock: 15",
    "valoresNuevos": "Stock: 13"
  }
]
```

## Colección de Postman

Puedes armar una colección rápida importando estas 6 peticiones como
requests GET individuales con el header `Authorization: Bearer {{token}}`
(usa una variable de entorno `{{token}}` con el JWT que te devuelve
`POST /api/auth/login`, y `{{base_url}}` = `http://localhost:8081/api`):

1. `{{base_url}}/reportes/movimientos`
2. `{{base_url}}/reportes/movimientos?tipoMovimiento=SALIDA&fechaInicio=2026-07-01T00:00:00&fechaFin=2026-07-31T23:59:59`
3. `{{base_url}}/reportes/movimientos?productoId=3`
4. `{{base_url}}/reportes/movimientos?bodegaId=1&tipoMovimiento=ENTRADA`
5. `{{base_url}}/reportes/auditoria?producto=Movimiento`
6. `{{base_url}}/reportes/auditoria?campoModificado=Stock&fechaInicio=2026-07-01T00:00:00&fechaFin=2026-07-31T23:59:59`
