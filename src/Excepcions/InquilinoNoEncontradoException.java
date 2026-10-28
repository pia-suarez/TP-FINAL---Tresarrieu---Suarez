package Excepcions;

/** Se lanza cuando se busca un pasajero por DNI y no está registrado. */
public class InquilinoNoEncontradoException extends Exception {

    public InquilinoNoEncontradoException(String dni) {
        super("No se encontró ningún pasajero con DNI " + dni + ".");
    }
}
