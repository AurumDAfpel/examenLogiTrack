package proyect.repository;

import proyect.model.Auditoria;
import proyect.model.TipoOperacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    List<Auditoria> findByUsuario(String usuario);
    List<Auditoria> findByTipoOperacion(TipoOperacion tipoOperacion);
}