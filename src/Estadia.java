import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Estadia {
    private LocalDate checkIn;
    private LocalDate checkOut;
    private double precioTotal;
    private Habitacion habitacion;
    private Inquilino inquilino;

    public Estadia(LocalDate checkIn, Habitacion habitacion, Inquilino inquilino) {
        this.checkIn = checkIn;
        this.habitacion = habitacion;
        this.inquilino = inquilino;
    }

    public double calcularTotal() {
        LocalDate fin = (checkOut != null) ? checkOut : LocalDate.now();
        long noches = Math.max(1, ChronoUnit.DAYS.between(checkIn, fin));
        precioTotal = noches * habitacion.calcularPrecio();
        return precioTotal;
    }

    public void cerrar() {
        this.checkOut = LocalDate.now();
        calcularTotal();
    }

    public boolean estaAbierta() { return checkOut == null; }

    public LocalDate getCheckIn() { return checkIn; }
    public LocalDate getCheckOut() { return checkOut; }
    public double getPrecioTotal() { return precioTotal; }
    public Habitacion getHabitacion() { return habitacion; }
    public Inquilino getInquilino() { return inquilino; }
}
