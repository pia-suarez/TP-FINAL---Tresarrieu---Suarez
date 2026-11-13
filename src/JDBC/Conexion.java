package JDBC;

import Excepcions.PersistenciaException;
import com.sun.tools.javac.Main;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion {

    Connection conn;

    public void createConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/tp_final", "progra2", "santosypia");

            System.out.println("JDBC.Conexion establecida con exito");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
        } catch (SQLException e) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, e);
            System.out.println("Error al conectar con la base de datos");
        }
    }

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

    public void closeConnection() {
        try {
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
