package JDBC;

import Clases.*;
import Enums.EstadoReserva;
import Excepciones.PersistenciaException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Cada reserva tiene un id autogenerado por la base.
 * Una reserva con id 0 todavía no fue guardada (INSERT); con id > 0 ya existe (UPDATE).
 */
public class ConexionReserva {

    public void guardar(Reserva r) throws PersistenciaException {
        boolean esNueva = (r.getId() == 0);
        String sql = esNueva
                ? "INSERT INTO reservas (dni_inquilino, numero_habitacion, fecha_inicio, fecha_fin, estado) "
                + "VALUES (?, ?, ?, ?, ?)"
                : "UPDATE reservas SET dni_inquilino=?, numero_habitacion=?, fecha_inicio=?, fecha_fin=?, estado=? "
                + "WHERE id=?";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, r.getInquilino().getDni());
            stmt.setInt(2, r.getHabitacion().getNumero());
            stmt.setDate(3, Date.valueOf(r.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(r.getFechaFin()));
            stmt.setString(5, r.getEstado().name());
            if (!esNueva) {
                stmt.setInt(6, r.getId());
            }

            stmt.executeUpdate();

            if (esNueva) {
                // Recuperamos el id que generó la base y se lo asignamos al objeto
                try (ResultSet keys = stmt.getGeneratedKeys()) {
                    if (keys.next()) {
                        r.setId(keys.getInt(1));
                    }
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al guardar la reserva", e);
        }
    }

    /**
     * Lee las reservas. Recibe los gestores ya cargados para reutilizar las MISMAS instancias de
     * Habitacion e Inquilino (Hotel.estaDisponible compara habitaciones con ==).
     */
    public List<Reserva> obtenerTodas(GestorGenerico<String, Inquilino> gestorInquilinos,
                                      GestorGenerico<Integer, Habitacion> gestorHabitaciones)
            throws PersistenciaException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT id, dni_inquilino, numero_habitacion, fecha_inicio, fecha_fin, estado FROM reservas";

        try (Connection conn = Conexion.obtener();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id");
                Inquilino inq = gestorInquilinos.buscar(rs.getString("dni_inquilino"));
                Habitacion hab = gestorHabitaciones.buscar(rs.getInt("numero_habitacion"));
                if (inq == null || hab == null) {
                    throw new PersistenciaException(
                            "La reserva " + id + " referencia un inquilino o habitación inexistente", null);
                }
                LocalDate inicio = rs.getDate("fecha_inicio").toLocalDate();
                LocalDate fin = rs.getDate("fecha_fin").toLocalDate();
                EstadoReserva estado = EstadoReserva.valueOf(rs.getString("estado"));

                reservas.add(new Reserva(id, inicio, fin, hab, inq, estado));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al recuperar las reservas", e);
        }
        return reservas;
    }
}