package Clases;

import Enums.EstadoHabitacion;
import Interfaces.IPrecio;

/**
 * Clase abstracta que representa una habitación del hotel.
 * Guarda lo común a todos los tipos (número, estado, motivo y precio base) y deja que cada
 * subclase (Individual, Doble, Suite) defina cómo se calcula su precio por noche.
 */
public abstract class Habitacion implements IPrecio {

    private int numero;
    private EstadoHabitacion estado;
    /** Explica por qué la habitación está en ese estado (ej: "Limpieza profunda"). */
    private String motivo;
    /** Precio por noche sin adicionales; las subclases lo usan en calcularPrecio(). */
    protected double precioBase;

    /** Toda habitación nueva arranca DISPONIBLE y sin motivo. */
    public Habitacion(int numero, double precioBase) {
        this.numero = numero;
        this.precioBase = precioBase;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.motivo = "";
    }

    /** Cambia el estado y el motivo juntos para que nunca queden desincronizados. */
    public void cambiarEstado(EstadoHabitacion nuevoEstado, String motivo) {
        this.estado = nuevoEstado;
        this.motivo = motivo;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setEstado(EstadoHabitacion estado) {
        this.estado = estado;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public void setPrecioBase(double precioBase) {
        this.precioBase = precioBase;
    }

    public int getNumero() { return numero; }
    public EstadoHabitacion getEstado() { return estado; }
    public String getMotivo() { return motivo; }
    public double getPrecioBase() { return precioBase; }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " #" + numero + " (" + estado + ")";
    }
}