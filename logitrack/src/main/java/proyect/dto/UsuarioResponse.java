package proyect.dto;

/**
 * Representa un usuario sin exponer la contraseña (ni siquiera el hash).
 * Se usa para las respuestas de la API en vez de devolver el modelo Usuario directo.
 */
public record UsuarioResponse(Long id, String username, String role) {
}
