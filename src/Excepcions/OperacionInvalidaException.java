package Excepcions;

/**
 * Se lanza cuando una operación no corresponde al estado actual
 * (ej: check-out sin check-in previo, check-in sin reserva vigente,
 * cancelar una reserva inexistente).
 */
public class OperacionInvalidaException extends Exception {

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}