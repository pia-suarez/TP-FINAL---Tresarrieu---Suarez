import java.time.LocalDate;

public final class Recepcionista extends Usuario{
    private Hotel hotel;

    public Recepcionista(String nombreUsuario, String contrasenia, Hotel hotel) {
        super(nombreUsuario, contrasenia);
        this.hotel = hotel;
    }

    @Override
    public String getPermisos() {
        return "RECEPCION: check-in y registro de reservas";
    }

    public Estadia hacerCheckIn(String dni, int numeroHabitacion) {
        return hotel.checkIn(dni, numeroHabitacion);
    }

    public Reserva registrarReserva(String dni, int numeroHabitacion, LocalDate inicio, LocalDate fin) {
        return hotel.reservar(dni, numeroHabitacion, inicio, fin);
    }

}
