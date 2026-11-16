package Clases;

/**
 * Habitación para dos personas. Con cama matrimonial el recargo es del 15%,
 * con camas separadas del 5% sobre el precio base.
 */
public final class Doble extends Habitacion {
    private boolean camaMatrimonial;

    public Doble(int numero, double precioBase, boolean camaMatrimonial) {
        super(numero, precioBase);
        this.camaMatrimonial = camaMatrimonial;
    }

    /** Polimorfismo: precio base con recargo según el tipo de cama. */
    @Override
    public double calcularPrecio() {
        return camaMatrimonial ? precioBase * 1.15 : precioBase * 1.05;
    }

    public boolean tieneCamaMatrimonial() { return camaMatrimonial; }
}