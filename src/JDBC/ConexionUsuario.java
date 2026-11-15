package JDBC;

import Clases.Administrador;
import Clases.Hotel;
import Clases.Recepcionista;
import Clases.Usuario;
import Excepcions.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Usuarios del sistema (Administrador y Recepcionista). La clave primaria es el nombre de usuario.
 * Las dos subclases se guardan en una única tabla: la columna "tipo" indica cuál es.
 */
public class ConexionUsuario {

    /** Inserta el usuario, o actualiza su contraseña y tipo si ya existe ese nombre de usuario. */
    public void guardar(Usuario u) throws PersistenciaException {
        String sql = "INSERT INTO usuarios (nombre_usuario, contrasena, tipo) VALUES (?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE contrasena=VALUES(contrasena), tipo=VALUES(tipo)";

        String tipo;
        if (u instanceof Administrador) {
            tipo = "ADMINISTRADOR";
        } else if (u instanceof Recepcionista) {
            tipo = "RECEPCIONISTA";
        } else {
            throw new PersistenciaException(
                    "Tipo de usuario desconocido: " + u.getClass().getSimpleName(), null);
        }

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, u.getNombreUsuario());
            stmt.setString(2, u.getContrasena());
            stmt.setString(3, tipo);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar el usuario " + u.getNombreUsuario(), e);
        }
    }

    /** Elimina el usuario con ese nombre de la base (si no existe no hace nada). */
    public void eliminar(String nombreUsuario) throws PersistenciaException {
        String sql = "DELETE FROM usuarios WHERE nombre_usuario = ?";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombreUsuario);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al eliminar el usuario " + nombreUsuario, e);
        }
    }

    /**
     * Lee todos los usuarios y reconstruye la subclase correcta según la columna "tipo".
     * Recibe el hotel porque cada Recepcionista trabaja sobre el hotel del sistema.
     */
    public List<Usuario> obtenerTodos(Hotel hotel) throws PersistenciaException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT nombre_usuario, contrasena, tipo FROM usuarios";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("nombre_usuario");
                String contrasena = rs.getString("contrasena");
                String tipo = rs.getString("tipo");

                switch (tipo) {
                    case "ADMINISTRADOR":
                        usuarios.add(new Administrador(nombre, contrasena));
                        break;
                    case "RECEPCIONISTA":
                        usuarios.add(new Recepcionista(nombre, contrasena, hotel));
                        break;
                    default:
                        throw new PersistenciaException(
                                "Tipo de usuario desconocido en la base: " + tipo, null);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener la lista de usuarios", e);
        }
        return usuarios;
    }
}