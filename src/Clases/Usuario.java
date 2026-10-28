package Clases;

public abstract class Usuario {

    private String nombreUsuario;
    private String contrasena;

    public Usuario(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }

    public abstract String getPermisos();

    public boolean autenticar(String contrasena) {
        return this.contrasena.equals(contrasena);
    }

    public String getNombreUsuario() { return nombreUsuario; }
}
