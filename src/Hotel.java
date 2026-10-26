import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hotel {
    private Map<Integer, Habitacion> habitaciones;
    private Map<String, Inquilino> inquilinos;
    private List<Reserva> reservas;
    private List<Estadia> estaddias;

    public Hotel() {
        this.habitaciones = new HashMap<>();
        this.inquilinos = new HashMap<>();
        this.reservas = new ArrayList<>();
        this.estaddias = new ArrayList<>();
    }

    public void agregarHabitacion(Habitacion h){
        habitaciones.put(h.getNumHab(),h);
    }


}
