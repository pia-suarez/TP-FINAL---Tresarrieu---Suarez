# TP Final Programación II – Sistema de administración de un hotel

**Autores:** Tresarrieu y Suarez

Programa de consola en Java que administra un hotel: habitaciones, pasajeros,
reservas, check-in y check-out. Hay dos tipos de usuario: **administrador** y
**recepcionista**. Los datos se guardan en una base de datos MySQL.

## Qué necesitás tener instalado
1. Java (JDK 17 o superior)
2. MySQL 8, encendido en tu compu
3. El archivo `mysql-connector-j-26.7.0.jar`, la librería que permite que Java
   se conecte con MySQL

## Cómo ponerlo a funcionar

### Paso 1 – Crear la base de datos (se hace una sola vez)
1. Abrí **MySQL Workbench** (se instala junto con MySQL).
2. Conectate con el usuario `root` (el administrador de MySQL) y la contraseña
   que elegiste al instalarlo.
3. Abrí el archivo `sql/schema.sql` y ejecutalo completo (botón del rayo).

Esto crea las tablas donde se guardan los datos. Al final de ese mismo archivo
también se crea el usuario `progra2` (clave `santosypia`), que es el que usa el
programa para entrar a MySQL. Si querés otro usuario o clave, se cambian en
`src/JDBC/Conexion.java`.

### Paso 2 – Agregar la librería
Copiá el archivo `.jar` dentro de la carpeta `lib/`. Si IntelliJ no lo reconoce:
*File > Project Structure > Libraries* y agregalo a mano.

### Paso 3 – Ejecutar
Abrí el proyecto en IntelliJ y corré la clase `Main`.

## Primer ingreso
El programa crea solo un administrador:
- Usuario: `admin`
- Contraseña: `admin123`

Desde el menú del administrador se crean los recepcionistas.

## Cosas a tener en cuenta
- Si MySQL no está disponible, el programa avisa y funciona igual, pero los
  datos se pierden al cerrarlo.
- Las contraseñas se guardan encriptadas (SHA-256), nunca en texto plano, para
  que nadie pueda leerlas mirando la base de datos.
- Si ya tenían la tabla `usuarios` de una versión anterior, ejecutar:
  `ALTER TABLE usuarios ADD COLUMN permisos VARCHAR(200) NULL;`

## Organización del código
| Carpeta | Qué contiene |
|---|---|
| `Clases` | Las clases principales: `Hotel`, `Habitacion` (y sus tipos), `Reserva`, `Estadia`, `Inquilino`, `Usuario` (y sus tipos), `GestorGenerico` |
| `Enums` | Los estados posibles: `EstadoHabitacion`, `EstadoReserva` |
| `Interfaces` | `IPrecio` |
| `Excepcions` | Las excepciones propias del sistema |
| `JDBC` | El código que habla con MySQL (una clase por tabla) |
| `Main` | Los menús que ve el usuario en consola |
