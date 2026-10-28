package Clases;

import Excepcions.*;
import Excepcions.HabitacionNoEncontradaException;
import Excepcions.InquilinoNoEncontradoException;

import java.time.LocalDate;

public final class Recepcionista extends Usuario {
    private Hotel hotel;

    public Recepcionista(String nombreUsuario, String contrasena, Hotel hotel) {
        super(nombreUsuario, contrasena);
        this.hotel = hotel;
    }

    @Override
    public String getPermisos() {
        return "RECEPCION: check-in y registro de reservas";
    }

    public Estadia hacerCheckIn(String dni, int numeroHabitacion)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            HabitacionNoDisponibleException {
        return hotel.checkIn(dni, numeroHabitacion);
    }

    public Reserva registrarReserva(String dni, int numeroHabitacion, LocalDate inicio, LocalDate fin)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            FechaInvalidaException, HabitacionNoDisponibleException {
        return hotel.reservar(dni, numeroHabitacion, inicio, fin);
    }
}
