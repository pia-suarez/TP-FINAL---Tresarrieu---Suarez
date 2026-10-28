package Excepcions;

/** Se lanza al intentar registrar un pasajero cuyo DNI ya existe en el sistema. */
public class InquilinoDuplicadoException extends Exception {

    public InquilinoDuplicadoException(String dni) {
        super("Ya existe un inquilino registrado con DNI " + dni + ".");
    }
}
