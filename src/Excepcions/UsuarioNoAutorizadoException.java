package Excepcions;

/**
 * Se lanza cuando un usuario intenta realizar una acción que su tipo de
 * usuario no permite (ej: un recepcionista intentando crear usuarios o hacer backup).
 */
public class UsuarioNoAutorizadoException extends Exception {

    public UsuarioNoAutorizadoException(String usuario, String accion) {
        super("El usuario '" + usuario + "' no tiene permiso para: " + accion + ".");
    }
}