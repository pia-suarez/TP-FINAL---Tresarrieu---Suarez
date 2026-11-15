package Clases;

import Excepcions.PersistenciaException;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class Administrador extends Usuario {

    public Administrador(String nombreUsuario, String contrasena) {
        super(nombreUsuario, contrasena);
    }

    @Override
    public String getPermisos() {
        return "ADMIN: gestión de usuarios y backups";
    }

    /** El administrador sí tiene permiso: crea un recepcionista que trabaja sobre el hotel dado. */
    @Override
    public Recepcionista crearUsuario(String nombreUsuario, String contrasena, Hotel hotel) {
        System.out.println("Usuario '" + nombreUsuario + "' creado por " + getNombreUsuario());
        return new Recepcionista(nombreUsuario, contrasena, hotel);
    }

    /**
     Guarda una copia de la información del hotel (habitaciones, pasajeros, reservas y estadías)
     en un archivo de texto. Cada sección empieza con un título "## NOMBRE" y cada registro
     es una línea con los campos separados por ";".
     No se incluyen los usuarios para no dejar contraseñas en un archivo de texto plano.
     */
    public void hacerBackup(Hotel hotel, String ruta) throws PersistenciaException {
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(ruta)))) {
            pw.println("# Backup generado por " + getNombreUsuario() + " el " + LocalDateTime.now());

            pw.println("## HABITACIONES (numero;tipo;precioBase;estado;motivo)");
            for (Habitacion h : hotel.getHabitaciones()) {
                pw.println(h.getNumero() + ";" + h.getClass().getSimpleName() + ";"
                        + h.getPrecioBase() + ";" + h.getEstado() + ";" + limpiar(h.getMotivo()));
            }

            pw.println("## INQUILINOS (dni;nombre;origen;domicilio)");
            for (Inquilino i : hotel.getInquilinos()) {
                pw.println(limpiar(i.getDni()) + ";" + limpiar(i.getNombre()) + ";"
                        + limpiar(i.getOrigen()) + ";" + limpiar(i.getDomicilioOrigen()));
            }

            pw.println("## RESERVAS (dni;habitacion;inicio;fin;estado)");
            for (Reserva r : hotel.getReservas()) {
                pw.println(limpiar(r.getInquilino().getDni()) + ";" + r.getHabitacion().getNumero() + ";"
                        + r.getFechaInicio() + ";" + r.getFechaFin() + ";" + r.getEstado());
            }

            pw.println("## ESTADIAS (dni;habitacion;checkIn;checkOut)");
            for (Estadia e : hotel.getEstadias()) {
                String salida = (e.getCheckOut() != null) ? e.getCheckOut().toString() : "";
                pw.println(limpiar(e.getInquilino().getDni()) + ";" + e.getHabitacion().getNumero() + ";"
                        + e.getCheckIn() + ";" + salida);
            }

            if (pw.checkError()) {
                throw new PersistenciaException("Error al escribir el backup en " + ruta, null);
            }
        } catch (IOException e) {
            throw new PersistenciaException("No se pudo crear el archivo de backup " + ruta, e);
        }
        System.out.println("Backup realizado por " + getNombreUsuario());
    }

    /** Evita que un ";" o un salto de línea escrito por el usuario rompa el formato del archivo. */
    private String limpiar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(';', ',').replace('\n', ' ').replace('\r', ' ');
    }
}