package proyect.controller;

import proyect.model.Auditoria;
import proyect.repository.AuditoriaRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auditorias")
public class AuditoriaController {

    private final AuditoriaRepository auditoriaRepository;

    public AuditoriaController(AuditoriaRepository auditoriaRepository) {
        this.auditoriaRepository = auditoriaRepository;
    }

    @GetMapping
    public List<Auditoria> listarTodas() { return auditoriaRepository.findAll(); }
}