package proyect.service;

import proyect.model.Producto;
import proyect.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductoService {
    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<Producto> listarTodos() { return productoRepository.findAll(); }
    public List<Producto> obtenerStockBajo(Integer limite) { return productoRepository.findByStockLessThan(limite); }
    public Producto guardar(Producto producto) { return productoRepository.save(producto); }
}
