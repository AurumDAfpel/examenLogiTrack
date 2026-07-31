package proyect.controller;

import proyect.model.Producto;
import proyect.service.ProductoService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public List<Producto> listar() { return productoService.listarTodos(); }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo(@RequestParam(defaultValue = "10") Integer limite) {
        return productoService.obtenerStockBajo(limite);
    }

    @PostMapping
    public Producto crear(@RequestBody Producto producto) { return productoService.guardar(producto); }
}
