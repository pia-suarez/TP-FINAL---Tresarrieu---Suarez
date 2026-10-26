import java.time.LocalDate;

public class Reserva {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private EstadoReserva estado;
    private Habitacion habitacion;
    private Inquilino inquilino;

    public Reserva(LocalDate fechaInicio, LocalDate fechaFin, Habitacion habitacion, Inquilino inquilino) {
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.habitacion = habitacion;
        this.inquilino = inquilino;
        this.estado = EstadoReserva.ACTIVA;
    }

    public void cancelar() {
        this.estado = EstadoReserva.CANCELADA;
    }

    // Dos rangos [inicio, fin) se solapan si uno empieza antes de que el otro termine
    public boolean solapaCon(Reserva otra) {
        return fechaInicio.isBefore(otra.fechaFin) && otra.fechaInicio.isBefore(fechaFin);
    }

    public boolean solapaCon(LocalDate inicio, LocalDate fin) {
        return fechaInicio.isBefore(fin) && inicio.isBefore(fechaFin);
    }

    public LocalDate getFechaInicio() { return fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public EstadoReserva getEstado() { return estado; }
    public Habitacion getHabitacion() { return habitacion; }
    public Inquilino getInquilino() { return inquilino; }
    public void setEstado(EstadoReserva estado) { this.estado = estado; }

}
