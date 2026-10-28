package Clases;

public final class Individual extends Habitacion {
    private boolean escritorio;

    public Individual(int numero, double precioBase, boolean escritorio) {
        super(numero, precioBase);
        this.escritorio = escritorio;
    }

    @Override
    public double calcularPrecio() {
        return escritorio ? precioBase * 1.10 : precioBase;
    }

    public boolean tieneEscritorio() { return escritorio; }
}
