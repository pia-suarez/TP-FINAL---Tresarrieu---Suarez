package Clases;

/**
 * Habitación para una persona. Si incluye escritorio tiene un 10% de recargo sobre el precio base.
 */
public final class Individual extends Habitacion {
    private boolean escritorio;

    public Individual(int numero, double precioBase, boolean escritorio) {
        super(numero, precioBase);
        this.escritorio = escritorio;
    }

    /** Polimorfismo: precio base, más 10% si tiene escritorio. */
    @Override
    public double calcularPrecio() {
        return escritorio ? precioBase * 1.10 : precioBase;
    }

    public boolean tieneEscritorio() { return escritorio; }
}