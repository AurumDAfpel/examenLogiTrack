package proyect.dto;

import proyect.model.TipoOperacion;

import java.time.LocalDateTime;

public record ReporteAuditoriaDTO(
        Long id,
        TipoOperacion tipoOperacion,
        LocalDateTime fechaHora,
        String usuario,
        String entidadAfectada,
        String valoresAnteriores,
        String valoresNuevos
) {
}
