public final class Individual extends Habitacion{
    private boolean escritorio;

    public Individual(int numHab, double precioBase, EstadoHabitacion estado, boolean escritorio) {
        super(numHab, precioBase, estado);
        this.escritorio = escritorio;
    }


    @Override
    public double calcularPago() {
        return 0;
    }
}
