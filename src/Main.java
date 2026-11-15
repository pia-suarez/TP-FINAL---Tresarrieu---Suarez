import Clases.*;
import Enums.EstadoHabitacion;
import Enums.EstadoReserva;
import Excepcions.*;
import JDBC.ConexionEstadia;
import JDBC.ConexionHabitacion;
import JDBC.ConexionInquilino;
import JDBC.ConexionReserva;
import JDBC.ConexionUsuario;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


interface AccionBD {
    void ejecutar() throws PersistenciaException;
}

private final Scanner in = new Scanner(System.in);
private final Hotel hotel = new Hotel();

/** Usuarios del sistema indexados por nombre de usuario (se cargan y se guardan en la base). */
private final Map<String, Usuario> usuarios = new HashMap<>();

private final ConexionHabitacion conHabitacion = new ConexionHabitacion();
private final ConexionInquilino conInquilino = new ConexionInquilino();
private final ConexionReserva conReserva = new ConexionReserva();
private final ConexionEstadia conEstadia = new ConexionEstadia();
private final ConexionUsuario conUsuario = new ConexionUsuario();

/** true si se pudo cargar desde la base; recién ahí se intenta guardar los cambios. */
private boolean usaBD = false;

void main() {
    cargarDesdeBD();

    // Si no hay ningún administrador (primera ejecución), se crea el inicial;
    // el resto de los usuarios los crea él desde su menú.
    if (!usuarios.containsKey("admin")) {
        Administrador admin = new Administrador("admin", "admin123");
        usuarios.put(admin.getNombreUsuario(), admin);
        persistir(() -> conUsuario.guardar(admin));
    }

    System.out.println("=== Sistema de administración de hotel ===");
    while (true) {
        Usuario u = login();
        if (u == null) {
            break;
        }
        System.out.println("Bienvenido " + u.getNombreUsuario() + " - " + u.getPermisos());
        if (u instanceof Administrador a) {
            menuAdministrador(a);
        } else if (u instanceof Recepcionista r) {
            menuRecepcionista(r);
        }
    }
    System.out.println("Hasta luego.");
}

// ------------------------------------------------------------------
// Login
// ------------------------------------------------------------------

/** Pide usuario y contraseña hasta que sean correctos. Devuelve null si se deja el usuario vacío (salir). */
private Usuario login() {
    while (true) {
        String nombre = leerTexto("\nUsuario (vacío para salir): ");
        if (nombre.isEmpty()) {
            return null;
        }
        String clave = leerTexto("Contraseña: ");
        Usuario u = usuarios.get(nombre);
        if (u != null && u.autenticar(clave)) {
            return u;
        }
        System.out.println("Usuario o contraseña incorrectos.");
    }
}

// ------------------------------------------------------------------
// Menú del administrador
// ------------------------------------------------------------------

private void menuAdministrador(Administrador admin) {
    boolean salir = false;
    while (!salir) {
        System.out.println("\n--- Menú administrador ---");
        System.out.println("1. Dar de alta una habitación");
        System.out.println("2. Crear usuario recepcionista");
        System.out.println("3. Hacer backup");
        System.out.println("4. Listar habitaciones");
        System.out.println("5. Listar usuarios y permisos");
        System.out.println("6. Eliminar usuario");
        System.out.println("0. Cerrar sesión");
        switch (leerEntero("Opción: ")) {
            case 1 -> altaHabitacion();
            case 2 -> crearRecepcionista(admin);
            case 3 -> hacerBackup(admin);
            case 4 -> listarTodasLasHabitaciones();
            case 5 -> listarUsuarios();
            case 6 -> eliminarUsuario(admin);
            case 0 -> salir = true;
            default -> System.out.println("Opción inválida.");
        }
    }
}

private void altaHabitacion() {
    System.out.println("Tipo: 1. Individual  2. Doble  3. Suite");
    int tipo = leerEntero("Tipo: ");
    if (tipo < 1 || tipo > 3) {
        System.out.println("Tipo inválido.");
        return;
    }
    int numero = leerEntero("Número de habitación: ");
    double precio = leerDouble("Precio base por noche: ");

    final Habitacion h;
    switch (tipo) {
        case 1 -> h = new Individual(numero, precio, leerSiNo("¿Tiene escritorio? (s/n): "));
        case 2 -> h = new Doble(numero, precio, leerSiNo("¿Cama matrimonial? (s/n): "));
        default -> h = new Suite(numero, precio, leerSiNo("¿Tiene jacuzzi? (s/n): "));
    }

    try {
        hotel.agregarHabitacion(h);
        persistir(() -> conHabitacion.guardar(h));
        System.out.println("Habitación agregada: " + h);
    } catch (OperacionInvalidaException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

/** Recibe un Usuario (no un Administrador) para que el permiso lo decida el polimorfismo de crearUsuario. */
private void crearRecepcionista(Usuario quien) {
    String nombre = leerTexto("Nombre de usuario nuevo: ");
    if (nombre.isEmpty() || usuarios.containsKey(nombre)) {
        System.out.println("El nombre está vacío o ya existe.");
        return;
    }
    String clave = leerTexto("Contraseña: ");
    try {
        Recepcionista r = quien.crearUsuario(nombre, clave, hotel);
        usuarios.put(nombre, r);
        persistir(() -> conUsuario.guardar(r));
    } catch (UsuarioNoAutorizadoException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void listarUsuarios() {
    System.out.println("\nUsuarios del sistema:");
    for (Usuario u : usuarios.values()) {
        System.out.println("- " + u.getNombreUsuario() + " | " + u.getPermisos());
    }
}

private void eliminarUsuario(Administrador admin) {
    listarUsuarios();
    String nombre = leerTexto("Usuario a eliminar (vacío para volver): ");
    if (nombre.isEmpty()) {
        return;
    }
    Usuario u = usuarios.get(nombre);
    if (u == null) {
        System.out.println("No existe el usuario '" + nombre + "'.");
        return;
    }
    if (u instanceof Administrador) {
        System.out.println("No se pueden eliminar usuarios administradores.");
        return;
    }
    usuarios.remove(nombre);
    persistir(() -> conUsuario.eliminar(nombre));
    System.out.println("Usuario '" + nombre + "' eliminado por " + admin.getNombreUsuario() + ".");
}

/** Genera un archivo de texto con la fecha de hoy en el nombre, en la carpeta donde se ejecuta el programa. */
private void hacerBackup(Administrador admin) {
    String ruta = "backup_" + LocalDate.now() + ".txt";
    try {
        admin.hacerBackup(hotel, ruta);
        System.out.println("Backup guardado en el archivo: " + ruta);
    } catch (PersistenciaException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

// ------------------------------------------------------------------
// Menú del recepcionista
// ------------------------------------------------------------------

private void menuRecepcionista(Recepcionista rec) {
    boolean salir = false;
    while (!salir) {
        System.out.println("\n--- Menú recepción ---");
        System.out.println("1.  Registrar pasajero");
        System.out.println("2.  Hacer reserva");
        System.out.println("3.  Cancelar reserva");
        System.out.println("4.  Check-in");
        System.out.println("5.  Check-out");
        System.out.println("6.  Consultar disponibilidad en un período");
        System.out.println("7.  Habitaciones ocupadas (con ocupantes)");
        System.out.println("8.  Habitaciones disponibles");
        System.out.println("9.  Habitaciones no disponibles (con motivo)");
        System.out.println("10. Cambiar estado de una habitación (mantenimiento, limpieza, etc.)");
        System.out.println("11. Informe e historial de un pasajero");
        System.out.println("12. Listar reservas");
        System.out.println("0.  Cerrar sesión");
        switch (leerEntero("Opción: ")) {
            case 1 -> registrarPasajero();
            case 2 -> hacerReserva(rec);
            case 3 -> cancelarReserva(rec);
            case 4 -> hacerCheckIn(rec);
            case 5 -> hacerCheckOut(rec);
            case 6 -> consultarDisponibilidad();
            case 7 -> listarOcupadas();
            case 8 -> listarPorEstado(EstadoHabitacion.DISPONIBLE, "Habitaciones disponibles");
            case 9 -> listarNoDisponibles();
            case 10 -> cambiarEstadoHabitacion();
            case 11 -> informePasajero();
            case 12 -> listarReservas();
            case 0 -> salir = true;
            default -> System.out.println("Opción inválida.");
        }
    }
}

private void registrarPasajero() {
    String nombre = leerTexto("Nombre y apellido: ");
    String dni = leerTexto("DNI: ");
    String origen = leerTexto("Origen (ciudad/país): ");
    String domicilio = leerTexto("Domicilio de origen: ");
    final Inquilino p = new Inquilino(nombre, dni, origen, domicilio);
    try {
        hotel.agregarInquilino(p);
        persistir(() -> conInquilino.guardar(p));
        System.out.println("Pasajero registrado.");
    } catch (InquilinoDuplicadoException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void hacerReserva(Recepcionista rec) {
    String dni = leerTexto("DNI del pasajero: ");
    int numero = leerEntero("Número de habitación: ");
    LocalDate inicio = leerFecha("Fecha de entrada (aaaa-mm-dd): ");
    LocalDate fin = leerFecha("Fecha de salida (aaaa-mm-dd): ");
    try {
        final Reserva r = rec.registrarReserva(dni, numero, inicio, fin);
        persistir(() -> conReserva.guardar(r));
        System.out.println("Reserva registrada para la habitación " + numero
                + " del " + inicio + " al " + fin + ".");
    } catch (InquilinoNoEncontradoException | HabitacionNoEncontradaException
             | FechaInvalidaException | HabitacionNoDisponibleException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void cancelarReserva(Recepcionista rec) {
    List<Reserva> activas = new ArrayList<>();
    for (Reserva r : hotel.getReservas()) {
        if (r.getEstado() == EstadoReserva.ACTIVA) {
            activas.add(r);
        }
    }
    if (activas.isEmpty()) {
        System.out.println("No hay reservas activas.");
        return;
    }
    for (int i = 0; i < activas.size(); i++) {
        System.out.println((i + 1) + ". " + describirReserva(activas.get(i)));
    }
    int opcion = leerEntero("Reserva a cancelar (0 para volver): ");
    if (opcion < 1 || opcion > activas.size()) {
        return;
    }
    final Reserva r = activas.get(opcion - 1);
    try {
        rec.cancelarReserva(r);
        persistir(() -> conReserva.guardar(r));
        System.out.println("Reserva cancelada.");
    } catch (OperacionInvalidaException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void hacerCheckIn(Recepcionista rec) {
    String dni = leerTexto("DNI del pasajero: ");
    int numero = leerEntero("Número de habitación: ");

    // Si el pasajero tenía una reserva para hoy, el check-in la deja COMPLETADA y hay que guardarla.
    final Reserva reservaDeHoy = hotel.reservaActivaEn(numero, LocalDate.now());
    try {
        final Estadia e = rec.hacerCheckIn(dni, numero);
        persistir(() -> {
            conEstadia.guardar(e);
            conHabitacion.guardar(e.getHabitacion());
            if (reservaDeHoy != null) {
                conReserva.guardar(reservaDeHoy);
            }
        });
        System.out.println("Check-in realizado en la habitación " + numero + ".");
        if (reservaDeHoy != null) {
            System.out.println("La reserva del pasajero quedó completada.");
        }
    } catch (InquilinoNoEncontradoException | HabitacionNoEncontradaException
             | HabitacionNoDisponibleException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void hacerCheckOut(Recepcionista rec) {
    String dni = leerTexto("DNI del pasajero: ");
    int numero = leerEntero("Número de habitación: ");

    // Buscamos la estadía abierta antes de cerrarla para poder persistirla después.
    Estadia abierta = null;
    for (Estadia e : hotel.getEstadias()) {
        if (e.estaAbierta() && e.getInquilino().getDni().equals(dni)
                && e.getHabitacion().getNumero() == numero) {
            abierta = e;
            break;
        }
    }
    try {
        double total = rec.hacerCheckOut(dni, numero);
        final Estadia cerrada = abierta;
        persistir(() -> {
            conEstadia.guardar(cerrada);
            conHabitacion.guardar(cerrada.getHabitacion());
        });
        System.out.printf("Check-out realizado. Total a cobrar: $%.2f%n", total);
    } catch (OperacionInvalidaException e) {
        System.out.println("Error: " + e.getMessage());
    }
}

private void consultarDisponibilidad() {
    int numero = leerEntero("Número de habitación: ");
    LocalDate inicio = leerFecha("Desde (aaaa-mm-dd): ");
    LocalDate fin = leerFecha("Hasta (aaaa-mm-dd): ");
    if (!fin.isAfter(inicio)) {
        System.out.println("La fecha final debe ser posterior a la inicial.");
        return;
    }
    if (hotel.estaDisponible(numero, inicio, fin)) {
        System.out.println("La habitación " + numero + " SE PUEDE ocupar en ese período.");
    } else {
        System.out.println("La habitación " + numero + " NO está disponible en ese período.");
    }
}

private void listarOcupadas() {
    System.out.println("\nHabitaciones ocupadas:");
    boolean hay = false;
    for (Habitacion h : hotel.getHabitaciones()) {
        if (h.getEstado() != EstadoHabitacion.OCUPADA) {
            continue;
        }
        hay = true;
        System.out.println("- " + h);
        for (Estadia e : hotel.getEstadias()) {
            if (e.estaAbierta() && e.getHabitacion() == h) {
                System.out.println("    Ocupante: " + e.getInquilino().getNombre()
                        + " (DNI " + e.getInquilino().getDni() + ") desde " + e.getCheckIn());
            }
        }
    }
    if (!hay) {
        System.out.println("(ninguna)");
    }
}

private void listarNoDisponibles() {
    System.out.println("\nHabitaciones no disponibles (mantenimiento/otros):");
    boolean hay = false;
    for (Habitacion h : hotel.getHabitaciones()) {
        if (h.getEstado() == EstadoHabitacion.MANTENIMIENTO) {
            hay = true;
            System.out.println("- " + h + " | motivo: " + h.getMotivo());
        }
    }
    if (!hay) {
        System.out.println("(ninguna)");
    }
}

private void cambiarEstadoHabitacion() {
    int numero = leerEntero("Número de habitación: ");
    final Habitacion hab = hotel.buscarHabitacion(numero);
    if (hab == null) {
        System.out.println("No existe la habitación " + numero + ".");
        return;
    }
    if (hab.getEstado() == EstadoHabitacion.OCUPADA) {
        System.out.println("La habitación está ocupada: hacé el check-out primero.");
        return;
    }
    System.out.println("Nuevo estado: 1. Disponible  2. Mantenimiento (limpieza, reparación, desinfección...)");
    int opcion = leerEntero("Opción: ");
    if (opcion == 1) {
        hab.cambiarEstado(EstadoHabitacion.DISPONIBLE, "");
    } else if (opcion == 2) {
        String motivo = leerTexto("Motivo: ");
        hab.cambiarEstado(EstadoHabitacion.MANTENIMIENTO, motivo);
    } else {
        System.out.println("Opción inválida.");
        return;
    }
    persistir(() -> conHabitacion.guardar(hab));
    System.out.println("Estado actualizado: " + hab);
}

private void informePasajero() {
    String dni = leerTexto("DNI del pasajero: ");
    Inquilino p = hotel.buscarInquilino(dni);
    if (p == null) {
        System.out.println("No hay ningún pasajero con DNI " + dni + ".");
        return;
    }
    System.out.println("\n" + p.generarInforme());

    System.out.println("Historial de estadías:");
    boolean hay = false;
    for (Estadia e : hotel.getEstadias()) {
        if (e.getInquilino().getDni().equals(dni)) {
            hay = true;
            String salida = e.estaAbierta() ? "(en curso)" : e.getCheckOut().toString();
            System.out.println("- Hab. " + e.getHabitacion().getNumero()
                    + " | " + e.getCheckIn() + " a " + salida);
        }
    }
    if (!hay) {
        System.out.println("(sin estadías)");
    }
}

private void listarReservas() {
    System.out.println("\nReservas:");
    if (hotel.getReservas().isEmpty()) {
        System.out.println("(ninguna)");
        return;
    }
    for (Reserva r : hotel.getReservas()) {
        System.out.println("- " + describirReserva(r));
    }
}

// ------------------------------------------------------------------
// Listados comunes
// ------------------------------------------------------------------

private void listarTodasLasHabitaciones() {
    System.out.println("\nHabitaciones:");
    if (hotel.getHabitaciones().isEmpty()) {
        System.out.println("(ninguna cargada)");
    }
    for (Habitacion h : hotel.getHabitaciones()) {
        System.out.printf("- %s | $%.2f por noche%n", h, h.calcularPrecio());
    }
}

private void listarPorEstado(EstadoHabitacion estado, String titulo) {
    System.out.println("\n" + titulo + ":");
    boolean hay = false;
    for (Habitacion h : hotel.getHabitaciones()) {
        if (h.getEstado() == estado) {
            hay = true;
            System.out.printf("- %s | $%.2f por noche%n", h, h.calcularPrecio());
        }
    }
    if (!hay) {
        System.out.println("(ninguna)");
    }
}

private String describirReserva(Reserva r) {
    return "Hab. " + r.getHabitacion().getNumero() + " | " + r.getInquilino().getNombre()
            + " (DNI " + r.getInquilino().getDni() + ") | "
            + r.getFechaInicio() + " a " + r.getFechaFin() + " | " + r.getEstado();
}

// ------------------------------------------------------------------
// Persistencia
// ------------------------------------------------------------------

/**
 * Carga habitaciones, inquilinos, reservas y estadías desde MySQL.
 * Las reservas y estadías se arman con las MISMAS instancias de habitación e inquilino
 * que usa el hotel, porque Hotel.estaDisponible compara habitaciones con ==.
 */
private void cargarDesdeBD() {
    try {
        GestorGenerico<Integer, Habitacion> gestorHab = new GestorGenerico<>();
        GestorGenerico<String, Inquilino> gestorInq = new GestorGenerico<>();

        for (Habitacion h : conHabitacion.obtenerTodas()) {
            hotel.agregarHabitacion(h);
            gestorHab.agregar(h.getNumero(), h);
        }
        for (Inquilino i : conInquilino.obtenerTodos()) {
            hotel.agregarInquilino(i);
            gestorInq.agregar(i.getDni(), i);
        }
        hotel.getReservas().addAll(conReserva.obtenerTodas(gestorInq, gestorHab));
        hotel.getEstadias().addAll(conEstadia.obtenerTodas(gestorInq, gestorHab));
        for (Usuario u : conUsuario.obtenerTodos(hotel)) {
            usuarios.put(u.getNombreUsuario(), u);
        }

        usaBD = true;
        System.out.println("Datos cargados desde la base: " + hotel.getHabitaciones().size()
                + " habitaciones, " + hotel.getInquilinos().size() + " pasajeros.");
    } catch (PersistenciaException | OperacionInvalidaException | InquilinoDuplicadoException e) {
        System.out.println("AVISO: no se pudo usar la base de datos (" + e.getMessage() + ").");
        System.out.println("El sistema funciona en memoria: los cambios se pierden al cerrar.");
    }
}

/** Ejecuta una operación de guardado solo si hay base; si falla avisa pero no corta el programa. */
private void persistir(AccionBD accion) {
    if (!usaBD) {
        return;
    }
    try {
        accion.ejecutar();
    } catch (PersistenciaException e) {
        System.out.println("AVISO: no se pudo guardar en la base: " + e.getMessage());
    }
}

// ------------------------------------------------------------------
// Lectura de datos por consola
// ------------------------------------------------------------------

private String leerTexto(String mensaje) {
    System.out.print(mensaje);
    return in.nextLine().trim();
}

private int leerEntero(String mensaje) {
    while (true) {
        try {
            return Integer.parseInt(leerTexto(mensaje));
        } catch (NumberFormatException e) {
            System.out.println("Ingresá un número entero válido.");
        }
    }
}

private double leerDouble(String mensaje) {
    while (true) {
        try {
            return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
        } catch (NumberFormatException e) {
            System.out.println("Ingresá un número válido.");
        }
    }
}

private LocalDate leerFecha(String mensaje) {
    while (true) {
        try {
            return LocalDate.parse(leerTexto(mensaje));
        } catch (DateTimeParseException e) {
            System.out.println("Fecha inválida, usá el formato aaaa-mm-dd.");
        }
    }
}

private boolean leerSiNo(String mensaje) {
    while (true) {
        String r = leerTexto(mensaje).toLowerCase();
        if (r.equals("s")) return true;
        if (r.equals("n")) return false;
        System.out.println("Respondé s o n.");
    }
}