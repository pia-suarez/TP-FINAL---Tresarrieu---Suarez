public abstract class Habitacion {

    private int numHab;
    private double precioBase;
    private EstadoHabitacion estado;

    public Habitacion(int numHab, double precioBase, EstadoHabitacion estado) {
        this.numHab = numHab;
        this.precioBase = precioBase;
        this.estado = estado;
    }

    private double calcularPrecio(){}

    private void cambiarEstado(EstadoHabitacion estado){
        this.estado = estado;
    }

}
