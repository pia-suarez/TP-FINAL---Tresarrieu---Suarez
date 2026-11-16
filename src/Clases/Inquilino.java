package Clases;

/**
 * Pasajero del hotel: la persona que reserva y ocupa las habitaciones.
 * El DNI es su identificador único dentro del sistema.
 */
public class Inquilino {

    private String nombre;
    private String dni;
    private String origen;
    private String domicilioOrigen;

    /** Ojo con el orden de los parámetros: primero nombre y después DNI. */
    public Inquilino(String nombre, String dni, String origen, String domicilioOrigen) {
        this.nombre = nombre;
        this.dni = dni;
        this.origen = origen;
        this.domicilioOrigen = domicilioOrigen;
    }

    /** Informe del pasajero con los datos pedidos: nombre, DNI, origen y domicilio de origen. */
    public String generarInforme() {
        return "Inquilino: " + nombre + "\n"
                + "DNI: " + dni + "\n"
                + "Origen: " + origen + "\n"
                + "Domicilio de origen: " + domicilioOrigen;
    }

    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getOrigen() { return origen; }
    public String getDomicilioOrigen() { return domicilioOrigen; }

}