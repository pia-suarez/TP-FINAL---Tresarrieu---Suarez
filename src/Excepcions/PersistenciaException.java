package Excepcions;

/**
 * Se lanza cuando falla la lectura o escritura de archivos (JSON, texto o binario).
 * Envuelve la excepción original (IOException, etc.) como causa, para no perder
 * el rastro del error real.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}

