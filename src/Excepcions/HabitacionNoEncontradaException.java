package Excepcions;

/** Se lanza cuando se busca una habitación por número y no existe en el hotel. */
public class HabitacionNoEncontradaException extends Exception {

    public HabitacionNoEncontradaException(int numeroHabitacion) {
        super("No existe la habitación número " + numeroHabitacion + ".");
    }
}