package com.logitrack.repository;

import com.logitrack.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    // Filtrar movimientos por rango de fechas (BETWEEN)
    List<Movimiento> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    // Filtrar movimientos pertenecientes a un producto en específico
    List<Movimiento> findByProductoId(Long productoId);
}