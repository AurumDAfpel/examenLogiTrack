package proyect.service;

import proyect.exception.ResourceNotFound;
import proyect.model.Bodega;
import proyect.repository.BodegaRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BodegaService {
    private final BodegaRepository bodegaRepository;

    public BodegaService(BodegaRepository bodegaRepository) {
        this.bodegaRepository = bodegaRepository;
    }

    public List<Bodega> listarTodas() { return bodegaRepository.findAll(); }
    public Bodega guardar(Bodega bodega) { return bodegaRepository.save(bodega); }

    public Bodega obtenerPorId(Long id) {
        return bodegaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Bodega no encontrada"));
    }
}