package JDBC;

import Excepcions.PersistenciaException;

import java.sql.*;

public class Conexion {

    public static Connection obtener() throws PersistenciaException {
        try {
            String url = "jdbc:mysql://localhost:3306/tp_final?useSSL=false&serverTimezone=UTC";
            String usuario = "progra2";
            String clave = "santosypia";

            return DriverManager.getConnection(url, usuario, clave);

        } catch (SQLException e) {
            throw new PersistenciaException("Error al conectar con la base de datos MySQL", e);
        }
    }
}