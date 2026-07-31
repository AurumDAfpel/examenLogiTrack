package proyect.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import proyect.model.Auditoria;

import java.time.LocalDateTime;

/**
 * Filtros dinamicos para el reporte de auditoria.
 *
 * NOTA IMPORTANTE: la entidad Auditoria no tiene columnas separadas para
 * "producto" ni "campoModificado" (solo guarda texto libre en
 * entidadAfectada, valoresAnteriores y valoresNuevos, ej: "Stock: 15").
 * Por eso estos dos filtros buscan coincidencias de texto (LIKE) dentro
 * de esos campos en vez de comparar contra una columna exacta.
 */
public class AuditoriaSpecification {

    public static Specification<Auditoria> conFiltros(String producto, String campoModificado,
                                                        LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (producto != null && !producto.isBlank()) {
                String like = "%" + producto.toLowerCase() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("entidadAfectada")), like),
                        cb.like(cb.lower(root.get("valoresAnteriores")), like),
                        cb.like(cb.lower(root.get("valoresNuevos")), like)
                ));
            }
            if (campoModificado != null && !campoModificado.isBlank()) {
                String like = "%" + campoModificado.toLowerCase() + "%";
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("valoresAnteriores")), like),
                        cb.like(cb.lower(root.get("valoresNuevos")), like)
                ));
            }
            if (fechaInicio != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("fechaHora"), fechaInicio));
            }
            if (fechaFin != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("fechaHora"), fechaFin));
            }

            return predicate;
        };
    }
}
