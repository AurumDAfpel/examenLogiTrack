package proyect.controller;

import proyect.model.Bodega;
import proyect.service.BodegaService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/bodegas")
public class BodegaController {

    private final BodegaService bodegaService;

    public BodegaController(BodegaService bodegaService) {
        this.bodegaService = bodegaService;
    }

    @GetMapping
    public List<Bodega> listar() { return bodegaService.listarTodas(); }

    @PostMapping
    public Bodega crear(@RequestBody Bodega bodega) { return bodegaService.guardar(bodega); }
}
