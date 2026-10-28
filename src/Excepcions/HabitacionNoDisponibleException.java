package Excepcions;

/**
 * Se lanza cuando se intenta reservar u ocupar una habitación que no está
 * disponible (ocupada, reservada en ese período o fuera de servicio).
 */
public class HabitacionNoDisponibleException extends Exception {

    public HabitacionNoDisponibleException(int numeroHabitacion, String motivo) {
        super("La habitación " + numeroHabitacion + " no está disponible: " + motivo);
    }
}
