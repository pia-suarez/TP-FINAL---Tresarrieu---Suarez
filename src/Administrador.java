public final class Administrador extends Usuario{

    public Administrador(String nombreUsuario, String contrasenia) {
        super(nombreUsuario, contrasenia);
    }

    @Override
    public String getNombreUsuario() {
        return "ADMIN: gestion de Usuarios y Backups";
    }

    public Recepcionista crearUsuario(String nombreUsuario, String contrasenia/*Hotel hotel*/){
        return Recepcionista;
    }

}
