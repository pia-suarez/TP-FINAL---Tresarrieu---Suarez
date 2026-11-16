# TP Final Programación II – Sistema de administración de un hotel

Trabajo práctico de Tresarrieu y Suarez. Aplicación de consola en Java que administra las
habitaciones, pasajeros, reservas y estadías de un hotel, con dos tipos de usuario
(administrador y recepcionista). La persistencia se hace con **JDBC sobre MySQL**.

## Requisitos
- JDK 17 o superior
- MySQL 8 (o compatible) corriendo en `localhost:3306`
- Conector `mysql-connector-j` (el `.jar`)

## Puesta en marcha
1. **Crear la base de datos.** Ejecutar el script como root:
   ```
   mysql -u root -p < sql/schema.sql
   ```
2. **Crear el usuario de MySQL** que usa la aplicación (usuario `progra2`, clave `santosypia`;
   los comandos están al final de `sql/schema.sql`). Si se quiere otro usuario o clave, se
   cambian en `src/JDBC/Conexion.java`.
3. **Agregar el conector.** Copiar `mysql-connector-j-26.7.0.jar` dentro de la carpeta `lib/` del proyecto
   (ver `lib/LEEME.txt`). IntelliJ ya lo busca ahí; si se usa otra versión del jar, ajustar
   `.idea/libraries/mysql_connector_j_26_7_0.xml` o agregarlo a mano en
   *File > Project Structure > Libraries*.
4. **Ejecutar** la clase `Main`.

Si ya tenían la tabla `usuarios` de una versión anterior, agregarle la columna de permisos:
`ALTER TABLE usuarios ADD COLUMN permisos VARCHAR(200) NULL;`

Si la base no está disponible el sistema avisa y funciona en memoria (los datos se pierden al cerrar).

## Usuario inicial
En la primera ejecución se crea el administrador `admin` / `admin123`. Desde su menú se crean
los recepcionistas.

Las contraseñas se guardan en la base como hash SHA-256, nunca en texto plano. Si la base tiene
usuarios de una versión anterior (con la contraseña en texto), se convierten solos al iniciar el programa.

## Estructura del código
| Paquete | Contenido |
|---|---|
| `Clases` | Modelo y lógica de negocio (`Hotel`, `Habitacion` y subclases, `Reserva`, `Estadia`, `Inquilino`, `Usuario` y subclases, `GestorGenerico`) |
| `Enums` | `EstadoHabitacion`, `EstadoReserva` |
| `Interfaces` | `IPrecio` |
| `Excepcions` | Excepciones personalizadas |
| `JDBC` | Acceso a MySQL (una clase por tabla) |
| `Main` | Menús de consola |
