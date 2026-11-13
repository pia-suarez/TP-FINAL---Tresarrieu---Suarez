package Clases;

import Enums.EstadoHabitacion;
import Enums.EstadoReserva;
import Interfaces.IPrecio;


public abstract class Habitacion implements IPrecio {

    private int numero;
    private EstadoHabitacion estado;
    private String motivo;
    protected double precioBase;

    public Habitacion(int numero, double precioBase) {
        this.numero = numero;
        this.precioBase = precioBase;
        this.estado = EstadoHabitacion.DISPONIBLE;
        this.motivo = "";
    }

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
