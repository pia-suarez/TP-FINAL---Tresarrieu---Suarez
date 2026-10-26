import java.util.Objects;

public abstract class Habitacion {

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

    public abstract double calcularPrecio();

    public void cambiarEstado(EstadoHabitacion nuevoEstado, String motivo) {
        this.estado = nuevoEstado;
        this.motivo = motivo;
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
