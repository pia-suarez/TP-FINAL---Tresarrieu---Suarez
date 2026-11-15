package Clases;

import Enums.EstadoHabitacion;
import Enums.EstadoReserva;
import Excepcions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 Clase principal del sistema: contiene la logica del negocio del hotel.
 Delega el almacenamiento de habitaciones e inquilinos en GestorGenerico.
 Las reservas y estadias se guardan en listas porque no tienen una clave natural de busqueda.
 */
public class Hotel {
    private GestorGenerico<Integer, Habitacion> habitaciones;
    private GestorGenerico<String, Inquilino> inquilinos;
    private List<Reserva> reservas;
    private List<Estadia> estadias;

    public Hotel() {
        this.habitaciones = new GestorGenerico<>();
        this.inquilinos = new GestorGenerico<>();
        this.reservas = new ArrayList<>();
        this.estadias = new ArrayList<>();
    }

    public void agregarHabitacion(Habitacion h) throws OperacionInvalidaException {
        if (!habitaciones.agregar(h.getNumero(), h)) {
            throw new OperacionInvalidaException(
                    "Ya existe una habitación con el número " + h.getNumero());
        }
    }

    public void agregarInquilino(Inquilino p) throws InquilinoDuplicadoException {
        if (!inquilinos.agregar(p.getDni(), p)) {
            throw new InquilinoDuplicadoException(p.getDni());
        }
    }

    /** Devuelve la habitación con ese número, o null si no existe. */
    public Habitacion buscarHabitacion(int numero) {
        return habitaciones.buscar(numero);
    }

    /** Devuelve el inquilino con ese DNI, o null si no está registrado. */
    public Inquilino buscarInquilino(String dni) {
        return inquilinos.buscar(dni);
    }

    /**
     Indica si la habitación se puede ocupar en el período [inicio, fin).
     No está disponible si no existe, si está en mantenimiento, si tiene una reserva activa
     que se solapa, o si hoy está ocupada y el período incluye el día de hoy
     (no se conoce la fecha de salida del ocupante actual).
     */
    public boolean estaDisponible(int numeroHabitacion, LocalDate inicio, LocalDate fin) {
        Habitacion h = habitaciones.buscar(numeroHabitacion);
        if (h == null || h.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
            return false;
        }
        LocalDate hoy = LocalDate.now();
        if (h.getEstado() == EstadoHabitacion.OCUPADA && !inicio.isAfter(hoy) && fin.isAfter(hoy)) {
            return false;
        }
        for (Reserva r : reservas) {
            if (r.getHabitacion() == h
                    && r.getEstado() == EstadoReserva.ACTIVA
                    && r.solapaCon(inicio, fin)) {
                return false;
            }
        }
        return true;
    }

    /**
     Devuelve la reserva ACTIVA de esa habitación que cubre la fecha dada,
     o null si no hay ninguna.
     */
    public Reserva reservaActivaEn(int numeroHabitacion, LocalDate fecha) {
        for (Reserva r : reservas) {
            if (r.getHabitacion().getNumero() == numeroHabitacion
                    && r.getEstado() == EstadoReserva.ACTIVA
                    && r.solapaCon(fecha, fecha.plusDays(1))) {
                return r;
            }
        }
        return null;
    }

    public Reserva reservar(String dni, int numeroHabitacion, LocalDate inicio, LocalDate fin)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            FechaInvalidaException, HabitacionNoDisponibleException {

        Inquilino p = inquilinos.buscar(dni);
        Habitacion h = habitaciones.buscar(numeroHabitacion);
        if (p == null) throw new InquilinoNoEncontradoException(dni);
        if (h == null) throw new HabitacionNoEncontradaException(numeroHabitacion);
        if (!fin.isAfter(inicio)) {
            throw new FechaInvalidaException("La fecha de salida debe ser posterior a la de entrada.");
        }
        if (inicio.isBefore(LocalDate.now())) {
            throw new FechaInvalidaException("La fecha de entrada no puede ser anterior a hoy.");
        }
        if (!estaDisponible(numeroHabitacion, inicio, fin)) {
            throw new HabitacionNoDisponibleException(numeroHabitacion,
                    "está ocupada, reservada o en mantenimiento en ese período");
        }
        Reserva r = new Reserva(inicio, fin, h, p);
        reservas.add(r);
        return r;
    }

    /** Cancela una reserva. Solo se pueden cancelar las reservas que están activas. */
    public void cancelarReserva(Reserva r) throws OperacionInvalidaException {
        if (r.getEstado() != EstadoReserva.ACTIVA) {
            throw new OperacionInvalidaException("Solo se pueden cancelar reservas activas.");
        }
        r.cancelar();
    }

    /**
     Registra el ingreso de un pasajero a una habitación.
     Si la habitación tiene una reserva activa para hoy hecha por otro pasajero, no se puede ocupar.
     Si la reserva es del mismo pasajero, queda COMPLETADA.
     */
    public Estadia checkIn(String dni, int numeroHabitacion)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            HabitacionNoDisponibleException {

        Inquilino p = inquilinos.buscar(dni);
        Habitacion h = habitaciones.buscar(numeroHabitacion);
        if (p == null) throw new InquilinoNoEncontradoException(dni);
        if (h == null) throw new HabitacionNoEncontradaException(numeroHabitacion);
        if (h.getEstado() != EstadoHabitacion.DISPONIBLE) {
            throw new HabitacionNoDisponibleException(numeroHabitacion,
                    "su estado actual es " + h.getEstado());
        }

        Reserva reserva = reservaActivaEn(numeroHabitacion, LocalDate.now());
        if (reserva != null && !reserva.getInquilino().getDni().equals(dni)) {
            throw new HabitacionNoDisponibleException(numeroHabitacion,
                    "está reservada por otro pasajero para hoy");
        }

        Estadia e = new Estadia(LocalDate.now(), h, p);
        h.cambiarEstado(EstadoHabitacion.OCUPADA, "Ocupada por " + p.getNombre());
        estadias.add(e);
        if (reserva != null) {
            reserva.setEstado(EstadoReserva.COMPLETADA);
        }
        return e;
    }

    public double checkOut(String dni, int numeroHabitacion) throws OperacionInvalidaException {
        for (Estadia e : estadias) {
            if (e.estaAbierta()
                    && e.getInquilino().getDni().equals(dni)
                    && e.getHabitacion().getNumero() == numeroHabitacion) {
                e.cerrar();
                e.getHabitacion().cambiarEstado(EstadoHabitacion.DISPONIBLE, "");
                return e.getPrecioTotal();
            }
        }
        throw new OperacionInvalidaException(
                "No hay una estadía abierta para ese inquilino y habitación.");
    }

    public Collection<Habitacion> getHabitaciones() { return habitaciones.listar(); }
    public Collection<Inquilino> getInquilinos() { return inquilinos.listar(); }
    public List<Reserva> getReservas() { return reservas; }
    public List<Estadia> getEstadias() { return estadias; }
}