package Clases;

import Excepcions.UsuarioNoAutorizadoException;

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

    /**
     Crear usuarios es una función exclusiva del administrador.
     Por defecto cualquier usuario no tiene ese permiso y se lanza la excepción;
     Administrador sobreescribe este método con la implementación real.
     */
    public Recepcionista crearUsuario(String nombreUsuario, String contrasena, Hotel hotel)
            throws UsuarioNoAutorizadoException {
        throw new UsuarioNoAutorizadoException(this.nombreUsuario, "crear usuarios");
    }

    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
}