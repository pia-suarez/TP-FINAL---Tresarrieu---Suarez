public final class Administrador extends Usuario{

    public Administrador(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public String getPermisos() {
        return "ADMIN: gestión de usuarios y backups";
    }

    public Recepcionista crearUsuario(String nombreUsuario, String contrasena, Hotel hotel) {
        System.out.println("Usuario '" + nombreUsuario + "' creado por " + getNombreUsuario());
        return new Recepcionista(nombreUsuario, contrasena, hotel);
    }

    public void hacerBackup() {
        System.out.println("Backup realizado por " + getNombreUsuario());
}
