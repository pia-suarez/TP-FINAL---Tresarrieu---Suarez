public final class Suite extends Habitacion {
    private boolean tieneJacuzzi;

    public Suite(int numHab, double precioBase, EstadoHabitacion estado, boolean tieneJacuzzi) {
        super(numHab, precioBase, estado);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    @Override
    public double calcularPago() {
        return 0;
    }
}
