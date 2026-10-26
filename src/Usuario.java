public abstract class Usuario {

    private String nombreUsuario;
    private String contrasenia;

    public Usuario(String nombreUsuario, String contrasenia) {
        this.nombreUsuario = nombreUsuario;
        this.contrasenia = contrasenia;
    }

    public abstract String getPermisos();

    public boolean autenticar(String contrasenia){
        return this.contrasenia.equals(contrasenia);
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }
}
