package Clases;


import Enums.EstadoHabitacion;
import Enums.EstadoReserva;
import Excepcions.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {
    private Map<Integer, Habitacion> habitaciones;
    private Map<String, Inquilino> inquilinos;
    private List<Reserva> reservas;
    private List<Estadia> estadias;

    public Hotel() {
        this.habitaciones = new HashMap<>();
        this.inquilinos = new HashMap<>();
        this.reservas = new ArrayList<>();
        this.estadias = new ArrayList<>();
    }

    public void agregarHabitacion(Habitacion h) {
        habitaciones.put(h.getNumero(), h);
    }

    public void agregarInquilino(Inquilino p) throws InquilinoDuplicadoException {
        if (inquilinos.containsKey(p.getDni())) {
            throw new InquilinoDuplicadoException(p.getDni());
        }
        inquilinos.put(p.getDni(), p);
    }

    public boolean estaDisponible(int numeroHabitacion, LocalDate inicio, LocalDate fin) {
        Habitacion h = habitaciones.get(numeroHabitacion);
        if (h == null || h.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
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

    public Reserva reservar(String dni, int numeroHabitacion, LocalDate inicio, LocalDate fin)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            FechaInvalidaException, HabitacionNoDisponibleException {

        Inquilino p = inquilinos.get(dni);
        Habitacion h = habitaciones.get(numeroHabitacion);
        if (p == null) throw new InquilinoNoEncontradoException(dni);
        if (h == null) throw new HabitacionNoEncontradaException(numeroHabitacion);
        if (!fin.isAfter(inicio)) {
            throw new FechaInvalidaException("La fecha de salida debe ser posterior a la de entrada.");
        }
        if (!estaDisponible(numeroHabitacion, inicio, fin)) {
            throw new HabitacionNoDisponibleException(numeroHabitacion,
                    "está ocupada, reservada o en mantenimiento en ese período");
        }
        Reserva r = new Reserva(inicio, fin, h, p);
        reservas.add(r);
        return r;
    }

    public Estadia checkIn(String dni, int numeroHabitacion)
            throws InquilinoNoEncontradoException, HabitacionNoEncontradaException,
            HabitacionNoDisponibleException {

        Inquilino p = inquilinos.get(dni);
        Habitacion h = habitaciones.get(numeroHabitacion);
        if (p == null) throw new InquilinoNoEncontradoException(dni);
        if (h == null) throw new HabitacionNoEncontradaException(numeroHabitacion);
        if (h.getEstado() != EstadoHabitacion.DISPONIBLE) {
            throw new HabitacionNoDisponibleException(numeroHabitacion,
                    "su estado actual es " + h.getEstado());
        }
        Estadia e = new Estadia(LocalDate.now(), h, p);
        h.cambiarEstado(EstadoHabitacion.OCUPADA, "Ocupada por " + p.getNombre());
        estadias.add(e);
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

    public Map<Integer, Habitacion> getHabitaciones() { return habitaciones; }
    public Map<String, Inquilino> getInquilinos() { return inquilinos; }
    public List<Reserva> getReservas() { return reservas; }
    public List<Estadia> getEstadias() { return estadias; }
}
