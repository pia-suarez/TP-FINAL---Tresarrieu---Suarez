import java.util.Objects;

public abstract class Habitacion implements IPago {

    private int numHab;
    private double precioBase;
    private EstadoHabitacion estado;

    public Habitacion(int numHab, double precioBase, EstadoHabitacion estado) {
        this.numHab = numHab;
        this.precioBase = precioBase;
        this.estado = estado;
    }


    private void cambiarEstado(EstadoHabitacion estado){
        this.estado = estado;
    }

    public int getNumHab() {
        return numHab;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Habitacion that)) return false;
        return numHab == that.numHab;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(numHab);
    }
}
