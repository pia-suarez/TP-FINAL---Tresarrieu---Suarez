public final class Suite extends Habitacion {
    private boolean tieneJacuzzi;

    public Suite(int numero, double precioBase, boolean tieneJacuzzi) {
        super(numero, precioBase);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    @Override
    public double calcularPrecio() {
        return tieneJacuzzi ? precioBase * 1.50 : precioBase * 1.30;
    }

    public boolean tieneJacuzzi() { return tieneJacuzzi; }
}
