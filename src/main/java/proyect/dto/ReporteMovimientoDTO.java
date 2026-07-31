package proyect.dto;

import proyect.model.TipoMovimiento;

import java.time.LocalDateTime;

public record ReporteMovimientoDTO(
        Long id,
        LocalDateTime fecha,
        TipoMovimiento tipoMovimiento,
        Long productoId,
        String productoNombre,
        Integer cantidad,
        Long bodegaOrigenId,
        String bodegaOrigenNombre,
        Long bodegaDestinoId,
        String bodegaDestinoNombre,
        String usuarioResponsable
) {
}
