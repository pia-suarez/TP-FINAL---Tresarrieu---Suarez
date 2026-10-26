public class Inquilino {
    private String dni;
    private String nombre;
    private String origen;
    private String domicilioOrigen;

    public Inquilino(String dni, String nombre, String origen, String domicilioOrigen) {
        this.dni = dni;
        this.nombre = nombre;
        this.origen = origen;
        this.domicilioOrigen = domicilioOrigen;
    }

    @Override
    public String toString() {
        return "inquilino:" + ", nombre='" + nombre + '\'' + "dni='" + dni + '\'' + ", origen='" + origen + '\'' + ", domicilioOrigen='" + domicilioOrigen + '\'' ;
    }

    public String getDni() {
        return dni;
    }
    public String getNombre() {
        return nombre;
    }
    public String getOrigen() {
        return origen;
    }
    public String getDomicilioOrigen() {
        return domicilioOrigen;
    }
}
