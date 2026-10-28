package Clases;

public final class Doble extends Habitacion {
    private boolean camaMatrimonial;

    public Doble(int numero, double precioBase, boolean camaMatrimonial) {
        super(numero, precioBase);
        this.camaMatrimonial = camaMatrimonial;
    }

    @Override
    public double calcularPrecio() {
        return camaMatrimonial ? precioBase * 1.15 : precioBase * 1.05;
    }

    public boolean tieneCamaMatrimonial() { return camaMatrimonial; }
}
