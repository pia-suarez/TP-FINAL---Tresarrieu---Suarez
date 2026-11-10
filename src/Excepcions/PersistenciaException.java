package Excepcions;

/**
 Se lanza cuando falla la lectura o escritura de archivos (JDBC).
 Envuelve la excepción original como causa, para no perder el rastro del error real.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

