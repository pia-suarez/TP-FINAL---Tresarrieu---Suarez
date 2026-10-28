package Clases;

public class Inquilino {

    private String nombre;
    private String dni;
    private String origen;
    private String domicilioOrigen;

    public Inquilino(String nombre, String dni, String origen, String domicilioOrigen) {
        this.nombre = nombre;
        this.dni = dni;
        this.origen = origen;
        this.domicilioOrigen = domicilioOrigen;
    }

    public String generarInforme() {
        return "Clases.Inquilino: " + nombre + "\n"
                + "DNI: " + dni + "\n"
                + "Origen: " + origen + "\n"
                + "Domicilio de origen: " + domicilioOrigen;
    }

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getOrigen() { return origen; }
    public String getDomicilioOrigen() { return domicilioOrigen; }
}
