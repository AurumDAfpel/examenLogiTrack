package proyect.controller;

import proyect.dto.UsuarioResponse;
import proyect.model.Usuario;
import proyect.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Gestion de usuarios. Protegido en SecurityConfig para que solo
 * usuarios con rol ADMIN puedan acceder ("/usuarios/**" -> hasRole("ADMIN")).
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@RequestBody Usuario usuario) {
        UsuarioResponse creado = usuarioService.crear(usuario);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id, Authentication auth) {
        usuarioService.eliminar(id, auth.getName());
        return ResponseEntity.noContent().build();
    }
}
