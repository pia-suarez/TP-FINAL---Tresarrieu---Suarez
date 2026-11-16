package Clases;

import Excepciones.*;

import java.time.LocalDate;

public final class Recepcionista extends Usuario {
    private Hotel hotel;

    public Recepcionista(String nombreUsuario, String contrasena, Hotel hotel) {
        super(nombreUsuario, contrasena);
        this.hotel = hotel;
    }

    @Override
    public String getPermisos() {
        return "RECEPCION: reservas, check-in y check-out";
    }

    public Estadia hacerCheckIn(String dni, int numeroHabitacion)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            HabitacionNoDisponibleException {
        return hotel.checkIn(dni, numeroHabitacion);
    }

    /** Cierra la estadía abierta del pasajero y devuelve el total a cobrar. */
    public double hacerCheckOut(String dni, int numeroHabitacion) throws OperacionInvalidaException {
        return hotel.checkOut(dni, numeroHabitacion);
    }

    public Reserva registrarReserva(String dni, int numeroHabitacion, LocalDate inicio, LocalDate fin)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            FechaInvalidaException, HabitacionNoDisponibleException {
        return hotel.reservar(dni, numeroHabitacion, inicio, fin);
    }

    public void cancelarReserva(Reserva reserva) throws OperacionInvalidaException {
        hotel.cancelarReserva(reserva);
    }
}