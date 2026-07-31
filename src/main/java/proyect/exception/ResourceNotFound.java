package proyect.exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String mensaje) {
        super(mensaje);
    }
}
