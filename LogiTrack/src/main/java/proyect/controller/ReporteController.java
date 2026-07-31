package proyect.controller;

import proyect.dto.ReporteAuditoriaDTO;
import proyect.dto.ReporteMovimientoDTO;
import proyect.exception.BadRequestException;
import proyect.model.TipoMovimiento;
import proyect.service.ReporteService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reportes")
public class ReporteController {

    private final ReporteService reporteService;

    public ReporteController(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @GetMapping("/movimientos")
    public List<ReporteMovimientoDTO> reporteMovimientos(
            @RequestParam(required = false) Long bodegaId,
            @RequestParam(required = false) Long productoId,
            @RequestParam(required = false) String tipoMovimiento,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        TipoMovimiento tipo = null;
        if (tipoMovimiento != null && !tipoMovimiento.isBlank()) {
            try {
                tipo = TipoMovimiento.valueOf(tipoMovimiento.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("tipoMovimiento inválido. Valores permitidos: ENTRADA, SALIDA, TRANSFERENCIA");
            }
        }

        return reporteService.reporteMovimientos(bodegaId, productoId, tipo, fechaInicio, fechaFin);
    }

    @GetMapping("/auditoria")
    public List<ReporteAuditoriaDTO> reporteAuditoria(
            @RequestParam(required = false) String producto,
            @RequestParam(required = false) String campoModificado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fechaFin) {

        return reporteService.reporteAuditoria(producto, campoModificado, fechaInicio, fechaFin);
    }
}
