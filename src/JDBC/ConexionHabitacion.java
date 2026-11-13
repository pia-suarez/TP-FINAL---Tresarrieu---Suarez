package JDBC;

import Clases.*;
import Enums.EstadoHabitacion;
import Excepcions.PersistenciaException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Las tres subclases (Individual, Doble, Suite) se guardan en una
 * única tabla: la columna "tipo" indica cuál es y la columna "extra" guarda su
 * atributo booleano propio (escritorio / cama matrimonial / jacuzzi).
 */
public class ConexionHabitacion {

    /** Inserta la habitación, o la actualiza si ya existe una con ese número. */
    public void guardar(Habitacion h) throws PersistenciaException {
        String sql = "INSERT INTO habitaciones (numero, tipo, precio_base, extra, estado, motivo) "
                + "VALUES (?, ?, ?, ?, ?, ?) "
                + "ON DUPLICATE KEY UPDATE tipo=VALUES(tipo), precio_base=VALUES(precio_base), "
                + "extra=VALUES(extra), estado=VALUES(estado), motivo=VALUES(motivo)";

        String tipo;
        boolean extra;
        if (h instanceof Individual) {
            tipo = "INDIVIDUAL";
            extra = ((Individual) h).tieneEscritorio();
        } else if (h instanceof Doble) {
            tipo = "DOBLE";
            extra = ((Doble) h).tieneCamaMatrimonial();
        } else if (h instanceof Suite) {
            tipo = "SUITE";
            extra = ((Suite) h).tieneJacuzzi();
        } else {
            throw new PersistenciaException(
                    "Tipo de habitación desconocido: " + h.getClass().getSimpleName(), null);
        }

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, h.getNumero());
            stmt.setString(2, tipo);
            stmt.setDouble(3, h.getPrecioBase());
            stmt.setBoolean(4, extra);
            stmt.setString(5, h.getEstado().name());
            stmt.setString(6, h.getMotivo());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar la habitación " + h.getNumero(), e);
        }
    }

    /** Lee todas las habitaciones y reconstruye la subclase correcta según la columna "tipo". */
    public List<Habitacion> obtenerTodas() throws PersistenciaException {
        List<Habitacion> habitaciones = new ArrayList<>();
        String sql = "SELECT numero, tipo, precio_base, extra, estado, motivo FROM habitaciones";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int numero = rs.getInt("numero");
                String tipo = rs.getString("tipo");
                double precioBase = rs.getDouble("precio_base");
                boolean extra = rs.getBoolean("extra");
                EstadoHabitacion estado = EstadoHabitacion.valueOf(rs.getString("estado"));
                String motivo = rs.getString("motivo");

                Habitacion h;
                switch (tipo) {
                    case "INDIVIDUAL":
                        h = new Individual(numero, precioBase, extra);
                        break;
                    case "DOBLE":
                        h = new Doble(numero, precioBase, extra);
                        break;
                    case "SUITE":
                        h = new Suite(numero, precioBase, extra);
                        break;
                    default:
                        throw new PersistenciaException(
                                "Tipo de habitación desconocido en la base: " + tipo, null);
                }

                // La base devuelve null si el motivo está vacío; el constructor usa "" por defecto.
                h.cambiarEstado(estado, motivo != null ? motivo : "");
                habitaciones.add(h);
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener la lista de habitaciones", e);
        }
        return habitaciones;
    }
}