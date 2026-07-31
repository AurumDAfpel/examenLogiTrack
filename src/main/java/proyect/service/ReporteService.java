package proyect.service;

import proyect.dto.ReporteAuditoriaDTO;
import proyect.dto.ReporteMovimientoDTO;
import proyect.model.Auditoria;
import proyect.model.Bodega;
import proyect.model.Movimiento;
import proyect.model.TipoMovimiento;
import proyect.repository.AuditoriaRepository;
import proyect.repository.MovimientosRepository;
import proyect.specification.AuditoriaSpecification;
import proyect.specification.MovimientoSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteService {

    private final MovimientosRepository movimientosRepository;
    private final AuditoriaRepository auditoriaRepository;

    public ReporteService(MovimientosRepository movimientosRepository, AuditoriaRepository auditoriaRepository) {
        this.movimientosRepository = movimientosRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    public List<ReporteMovimientoDTO> reporteMovimientos(Long bodegaId, Long productoId, TipoMovimiento tipoMovimiento,
                                                           LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Movimiento> movimientos = movimientosRepository.findAll(
                MovimientoSpecification.conFiltros(bodegaId, productoId, tipoMovimiento, fechaInicio, fechaFin)
        );

        return movimientos.stream().map(this::aDTO).toList();
    }

    public List<ReporteAuditoriaDTO> reporteAuditoria(String producto, String campoModificado,
                                                        LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Auditoria> auditorias = auditoriaRepository.findAll(
                AuditoriaSpecification.conFiltros(producto, campoModificado, fechaInicio, fechaFin)
        );

        return auditorias.stream().map(this::aDTO).toList();
    }

    private ReporteMovimientoDTO aDTO(Movimiento m) {
        Bodega origen = m.getBodegaOrigen();
        Bodega destino = m.getBodegaDestino();

        return new ReporteMovimientoDTO(
                m.getId(),
                m.getFecha(),
                m.getTipoMovimiento(),
                m.getProducto() != null ? m.getProducto().getId() : null,
                m.getProducto() != null ? m.getProducto().getNombre() : null,
                m.getCantidad(),
                origen != null ? origen.getId() : null,
                origen != null ? origen.getNombre() : null,
                destino != null ? destino.getId() : null,
                destino != null ? destino.getNombre() : null,
                m.getUsuarioResponsable()
        );
    }

    private ReporteAuditoriaDTO aDTO(Auditoria a) {
        return new ReporteAuditoriaDTO(
                a.getId(),
                a.getTipoOperacion(),
                a.getFechaHora(),
                a.getUsuario(),
                a.getEntidadAfectada(),
                a.getValoresAnteriores(),
                a.getValoresNuevos()
        );
    }
}
