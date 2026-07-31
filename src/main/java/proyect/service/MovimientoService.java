package proyect.service;

import proyect.exception.BadRequestException;
import proyect.exception.ResourceNotFound;
import proyect.model.*;
import proyect.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MovimientoService {

    private final MovimientosRepository movimientoRepository;
    private final ProductoRepository productoRepository;
    private final AuditoriaRepository auditoriaRepository;

    public MovimientoService(MovimientosRepository movimientoRepository, ProductoRepository productoRepository, AuditoriaRepository auditoriaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.productoRepository = productoRepository;
        this.auditoriaRepository = auditoriaRepository;
    }

    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimiento, String username) {
        Producto producto = productoRepository.findById(movimiento.getProducto().getId())
                .orElseThrow(() -> new ResourceNotFound("Producto no encontrado"));

        int cantidad = movimiento.getCantidad();
        if (cantidad <= 0) throw new BadRequestException("La cantidad debe ser mayor a 0");

        int stockAnterior = producto.getStock();

        switch (movimiento.getTipoMovimiento()) {
            case ENTRADA -> producto.setStock(stockAnterior + cantidad);
            case SALIDA, TRANSFERENCIA -> {
                if (stockAnterior < cantidad) throw new BadRequestException("Stock insuficiente");
                producto.setStock(stockAnterior - cantidad);
            }
        }

        productoRepository.save(producto);
        movimiento.setProducto(producto);
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setUsuarioResponsable(username);

        Movimiento guardado = movimientoRepository.save(movimiento);

        // Guardar Auditoría
        Auditoria audit = new Auditoria(null, TipoOperacion.INSERT, LocalDateTime.now(), username,
                "Movimiento", "Stock: " + stockAnterior, "Stock: " + producto.getStock());
        auditoriaRepository.save(audit);

        return guardado;
    }
}