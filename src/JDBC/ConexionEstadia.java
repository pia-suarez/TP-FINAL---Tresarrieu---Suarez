package JDBC;

import Clases.*;
import Excepcions.PersistenciaException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cada estadía tiene un id autogenerado por la base.
 * Una estadía está abierta cuando fecha_salida es NULL (no hace falta una columna "activa").
 */
public class ConexionEstadia {

    public void guardar(Estadia est) throws PersistenciaException {
        boolean esNueva = (est.getId() == 0);
        String sql = esNueva
                ? "INSERT INTO estadias (dni_inquilino, numero_habitacion, fecha_entrada, fecha_salida) "
                + "VALUES (?, ?, ?, ?)"
                : "UPDATE estadias SET dni_inquilino=?, numero_habitacion=?, fecha_entrada=?, fecha_salida=? "
                + "WHERE id=?";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, est.getInquilino().getDni());
            stmt.setInt(2, est.getHabitacion().getNumero());
            stmt.setDate(3, Date.valueOf(est.getCheckIn()));
            if (est.getCheckOut() != null) {
                stmt.setDate(4, Date.valueOf(est.getCheckOut()));
            } else {
                stmt.setNull(4, Types.DATE);   // setDate(4, null) no es seguro en todos los drivers
            }
            if (!esNueva) {
                stmt.setInt(5, est.getId());
            }

            stmt.executeUpdate();

            if (esNueva) {
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        est.setId(keys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar la estadía", e);
        }
    }

    /** Igual que en ConexionReserva: reutiliza las instancias ya cargadas de inquilinos y habitaciones. */
    public List<Estadia> obtenerTodas(GestorGenerico<String, Inquilino> gestorInquilinos,
                                      GestorGenerico<Integer, Habitacion> gestorHabitaciones)
            throws PersistenciaException {
        List<Estadia> estadias = new ArrayList<>();
        String sql = "SELECT id, dni_inquilino, numero_habitacion, fecha_entrada, fecha_salida FROM estadias";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Inquilino inq = gestorInquilinos.buscar(rs.getString("dni_inquilino"));
                Habitacion hab = gestorHabitaciones.buscar(rs.getInt("numero_habitacion"));
                if (inq == null || hab == null) {
                    throw new PersistenciaException(
                            "La estadía " + id + " referencia un inquilino o habitación inexistente", null);
                }
                LocalDate entrada = rs.getDate("fecha_entrada").toLocalDate();
                Date salidaSql = rs.getDate("fecha_salida");
                LocalDate salida = (salidaSql != null) ? salidaSql.toLocalDate() : null;

                estadias.add(new Estadia(id, entrada, salida, hab, inq));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al cargar las estadías", e);
        }
        return estadias;
    }
}