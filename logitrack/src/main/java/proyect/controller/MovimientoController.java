package proyect.controller;

import proyect.model.Movimiento;
import proyect.service.MovimientoService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;

    public MovimientoController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public List<Movimiento> listar() { return movimientoService.listarTodos(); }

    @PostMapping
    public Movimiento registrar(@RequestBody Movimiento movimiento, Authentication auth) {
        return movimientoService.registrarMovimiento(movimiento, auth.getName());
    }
}
