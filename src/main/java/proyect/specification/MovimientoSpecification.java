package proyect.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import proyect.model.Movimiento;
import proyect.model.TipoMovimiento;

import java.time.LocalDateTime;

/**
 * Filtros dinamicos para el reporte de movimientos.
 * Cada filtro es opcional: si el parametro es null, simplemente no se aplica.
 */
public class MovimientoSpecification {

    public static Specification<Movimiento> conFiltros(Long bodegaId, Long productoId, TipoMovimiento tipoMovimiento,
                                                         LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (bodegaId != null) {
                // La bodega puede aparecer como origen o como destino del movimiento
                predicate = cb.and(predicate, cb.or(
                        cb.equal(root.get("bodegaOrigen").get("id"), bodegaId),
                        cb.equal(root.get("bodegaDestino").get("id"), bodegaId)
                ));
            }
            if (productoId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("producto").get("id"), productoId));
            }
            if (tipoMovimiento != null) {
                predicate = cb.and(predicate, cb.equal(root.get("tipoMovimiento"), tipoMovimiento));
            }
            if (fechaInicio != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fecha"), fechaInicio));
            }
            if (fechaFin != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fecha"), fechaFin));
            }

            return predicate;
        };
    }
}
