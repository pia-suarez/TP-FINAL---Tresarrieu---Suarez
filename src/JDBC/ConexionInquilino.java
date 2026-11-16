package JDBC;

import Clases.Inquilino;
import Excepciones.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Inquilino (pasajero). La clave primaria es el DNI. */
public class ConexionInquilino {

    /** Inserta el inquilino, o actualiza sus datos si el DNI ya existe. */
    public void guardar(Inquilino inq) throws PersistenciaException {
        String sql = "INSERT INTO inquilinos (dni, nombre, origen, domicilio) VALUES (?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE nombre=VALUES(nombre), origen=VALUES(origen), "
                + "domicilio=VALUES(domicilio)";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, inq.getDni());
            stmt.setString(2, inq.getNombre());
            stmt.setString(3, inq.getOrigen());
            stmt.setString(4, inq.getDomicilioOrigen());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar inquilino: " + inq.getDni(), e);
        }
    }

    public List<Inquilino> obtenerTodos() throws PersistenciaException {
        List<Inquilino> inquilinos = new ArrayList<>();
        String sql = "SELECT dni, nombre, origen, domicilio FROM inquilinos";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // OJO: el constructor de Inquilino recibe (nombre, dni, origen, domicilio)
                inquilinos.add(new Inquilino(
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("origen"),
                        rs.getString("domicilio")
                ));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar la lista de inquilinos", e);
        }
        return inquilinos;
    }
}