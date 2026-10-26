public final class Doble extends Habitacion {
    private boolean camaMatrimonial;

    public Doble(int numHab, double precioBase, EstadoHabitacion estado, boolean camaMatrimonial) {
        super(numHab, precioBase, estado);
        this.camaMatrimonial = camaMatrimonial;
    }

    @Override
    public double calcularPago() {
        return 0;
    }
}
