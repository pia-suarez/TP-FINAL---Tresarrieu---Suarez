package Clases;

public final class Administrador extends Usuario {

    public Administrador(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public String getPermisos() {
        return "ADMIN: gestión de usuarios y backups";
    }

    /** El administrador sí tiene permiso: crea un recepcionista que trabaja sobre el hotel dado. */
    @Override
    public Recepcionista crearUsuario(String nombreUsuario, String contrasena, Hotel hotel) {
        System.out.println("Usuario '" + nombreUsuario + "' creado por " + getNombreUsuario());
        return new Recepcionista(nombreUsuario, contrasena, hotel);
    }

    public void hacerBackup() {
        System.out.println("Backup realizado por " + getNombreUsuario());
    }
}