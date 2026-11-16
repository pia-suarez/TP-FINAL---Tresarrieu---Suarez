package Clases;

/**
 * Habitación de categoría superior. Recargo del 30% sobre el precio base,
 * o del 50% si incluye jacuzzi.
 */
public final class Suite extends Habitacion {
    private boolean tieneJacuzzi;

    public Suite(int numero, double precioBase, boolean tieneJacuzzi) {
        super(numero, precioBase);
        this.tieneJacuzzi = tieneJacuzzi;
    }

    /** Polimorfismo: precio base con recargo según tenga o no jacuzzi. */
    @Override
    public double calcularPrecio() {
        return tieneJacuzzi ? precioBase * 1.50 : precioBase * 1.30;
    }

    public boolean tieneJacuzzi() { return tieneJacuzzi; }
}