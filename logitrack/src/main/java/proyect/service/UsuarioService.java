package proyect.service;

import proyect.dto.UsuarioResponse;
import proyect.exception.BadRequestException;
import proyect.exception.ResourceNotFound;
import proyect.model.Usuario;
import proyect.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UsuarioService {

    private static final Set<String> ROLES_VALIDOS = Set.of("ROLE_ADMIN", "ROLE_EMPLEADO");

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getUsername(), u.getRole()))
                .toList();
    }

    public UsuarioResponse crear(Usuario usuario) {
        if (usuario.getUsername() == null || usuario.getUsername().isBlank()) {
            throw new BadRequestException("El nombre de usuario es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().length() < 6) {
            throw new BadRequestException("La contraseña debe tener al menos 6 caracteres");
        }
        if (usuario.getRole() == null || !ROLES_VALIDOS.contains(usuario.getRole())) {
            throw new BadRequestException("El rol debe ser ROLE_ADMIN o ROLE_EMPLEADO");
        }
        if (usuarioRepository.existsByUsername(usuario.getUsername())) {
            throw new BadRequestException("El usuario ya existe");
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        Usuario guardado = usuarioRepository.save(usuario);
        return new UsuarioResponse(guardado.getId(), guardado.getUsername(), guardado.getRole());
    }

    public void eliminar(Long id, String usernameActual) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Usuario no encontrado"));

        if (usuario.getUsername().equalsIgnoreCase(usernameActual)) {
            throw new BadRequestException("No puedes eliminar tu propio usuario");
        }

        usuarioRepository.delete(usuario);
    }
}
