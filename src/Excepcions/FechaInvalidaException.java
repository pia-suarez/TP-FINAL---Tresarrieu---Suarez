package Excepcions;

/**
 * Se lanza cuando las fechas de una reserva o estadía son inconsistentes
 * (ej: salida anterior a la entrada, o entrada en el pasado).
 */
public class FechaInvalidaException extends Exception {

    public FechaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
