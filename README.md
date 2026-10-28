# 🎬 Sistema de Gestión de Películas (H2 & Java)

**Autor:** Robert  
**Tecnologías:** Java 17, H2 Database, Maven, JDBC  
**Arquitectura:** Aplicación de consola (CLI) con base de datos relacional embebida.

---

## 📖 1. Resumen del Proyecto
Este proyecto consiste en una aplicación de gestión por consola diseñada para administrar un catálogo de películas. A diferencia de un CRUD básico, el sistema implementa un modelo relacional complejo de **5 tablas** que permite asociar películas con sus directores, géneros y actores.

El objetivo principal es demostrar el dominio de la conexión a bases de datos mediante JDBC, la ejecución de consultas SQL avanzadas (JOINs y agregaciones) y la aplicación de buenas prácticas de programación (separación de responsabilidades, uso de `PreparedStatement` y control de excepciones).

---

## 🏗️ 2. Descomposición del Programa (Arquitectura)

El código está estructurado siguiendo el principio de **separación de responsabilidades**, dividiendo el proyecto en dos clases principales:

### A. `DatabaseManager.java` (Capa de Datos)
Es el motor de la base de datos. Se encarga exclusivamente de la infraestructura:
*   **Gestión de la conexión:** Proporciona el método `getConnection()` que utiliza el driver JDBC para conectar con H2.
*   **Inicialización (DDL):** El método `init()` ejecuta los scripts SQL necesarios para crear las 5 tablas (`genero`, `director`, `actor`, `pelicula` y `pelicula_actor`) si no existen.
*   **Carga inicial (DML):** Inserta automáticamente los datos de prueba la primera vez que se arranca el programa, para que la aplicación nunca esté vacía.

### B. `GestionPeliculas.java` (Capa de Presentación y Lógica)
Es la interfaz de usuario y el cerebro de la aplicación:
*   **Interfaz (UI):** Dibuja un menú interactivo por consola utilizando códigos de escape ANSI para darle color y formato visual.
*   **Control de flujo:** Utiliza un bucle `do-while` y un bloque `switch` para dirigir al usuario a la acción que desea realizar.
*   **Lógica de Negocio:** Ejecuta las consultas SQL (SELECT, INSERT, DELETE) y formatea los resultados obtenidos de la base de datos para mostrarlos de forma legible.
*   **Validación:** Incluye métodos auxiliares (como `leerInt`) para evitar que el programa se rompa si el usuario introduce letras en lugar de números.

---

## ⚙️ 3. Funcionamiento Básico (Flujo del Programa)

El funcionamiento de la aplicación se divide en los siguientes pilares:

1.  **Arranque Seguro:** Al iniciar, el programa asegura que las tablas existan. Si la base de datos está vacía, la puebla con datos de ejemplo (Inception, Pulp Fiction, Barbie).
2.  **Menú Interactivo:** El usuario navega por las opciones escribiendo números. Cada vez que se completa una acción, el programa hace una pausa hasta que el usuario pulsa `ENTER`, evitando que los resultados desaparezcan de golpe.
3.  **Consultas Relacionales (Lectura):**
    *   **Consulta Simple:** Muestra la tabla `pelicula` de forma directa.
    *   **Consulta JOIN:** Cruza las 4 tablas para mostrar una ficha completa de la película (Título, Año, Director, Género y Actor).
    *   **Agregación:** Utiliza funciones matemáticas de SQL (`COUNT`) y agrupaciones (`GROUP BY`) para contar cuántas películas hay de cada género.
4.  **Gestión Dinámica (Escritura/Borrado):**
    *   **Añadir:** Para que el usuario no tenga que memorizar IDs, el programa muestra listas numeradas de géneros, directores y actores. Si el actor no existe, permite crearlo en el momento.
    *   **Borrar:** Para evitar errores de Clave Foránea (FK), el programa borra primero la relación en la tabla intermedia (`pelicula_actor`) y luego la película en sí.
5.  **Buscador (LIKE):** Implementa un motor de búsqueda parcial e insensible a mayúsculas, permitiendo encontrar películas por el título, o filtrar todas las películas en las que participa un actor o director.

---

## 📊 4. Esquema de la Base de Datos

El diseño relacional garantiza la integridad de los datos mediante Claves Primarias (PK) y Claves Foráneas (FK).

| Tabla | Tipo de Entidad | Columnas Principales | Relaciones (Claves Foráneas) |
| :--- | :--- | :--- | :--- |
| **`genero`** | Catálogo | `id_genero` (PK), `nombre` | Ninguna |
| **`director`** | Catálogo | `id_director` (PK), `nombre` | Ninguna |
| **`actor`** | Catálogo | `id_actor` (PK), `nombre` | Ninguna |
| **`pelicula`** | Entidad Principal | `id_pelicula` (PK), `titulo`, `anio` | **FK** `id_genero`, **FK** `id_director` |
| **`pelicula_actor`** | Tabla Intermedia | `id_pelicula`, `id_actor` | **PK Compuesta**, **FK** a `pelicula`, **FK** a `actor` |

---

## 📝 5. Pseudocódigo del Bucle Principal

Representación algorítmica del método `main`:

```text
ALGORITMO GestionPeliculas
    // 1. Preparación del entorno
    INICIALIZAR BaseDeDatos (Crear tablas y datos por defecto)
    VARIABLE opcion = -1

    // 2. Bucle principal de la interfaz
    HACER
        LIMPIAR PANTALLA
        IMPRIMIR Menú de opciones (1: Listar, 2: JOIN, 3: Stats, 4: Añadir, 5: Borrar, 6: Buscar, 0: Salir)
        LEER opcion

        INTENTAR
            SEGÚN opcion HACER:
                CASO 1: EjecutarConsultaSimple()
                CASO 2: EjecutarConsultaJOIN()
                CASO 3: EjecutarConsultaAgregacion()
                CASO 4: FlujoInsertarPelicula() // Incluye submenús para elegir/crear Género, Director y Actor
                CASO 5: FlujoBorrarPelicula()   // Pide ID, borra relación N:M y luego la película
                CASO 6: FlujoBuscarPelicula()   // Filtro dinámico por Título, Actor o Director
                CASO 0: Imprimir "Saliendo..."
                DEFECTO: Imprimir "Opción no válida"
            
            PAUSAR (Esperar tecla ENTER)
            
        CAPTURAR ExcepcionSQL:
            Imprimir error controlado (Ej: Fallo de integridad referencial)
            
    MIENTRAS opcion SEA DISTINTO DE 0
FIN ALGORITMO
```

## 🚀 6. Instalación y Ejecución
### Requisitos previos
*   **Java Development Kit (JDK) 17** o superior instalado en el sistema.
*   **Maven** instalado (o utilizar el gestor de dependencias integrado en IntelliJ IDEA).

### Opción A: Ejecutar desde IntelliJ IDEA (Recomendado)
1. Abre el proyecto en IntelliJ IDEA (`File → Open`).
2. Espera unos segundos a que Maven descargue la dependencia de H2 (verás `h2-2.2.224.jar` en *External Libraries*).
3. Abre la clase `src/main/java/com/ejemplo/GestionPeliculas.java`.
4. Haz clic en el **botón verde ▶** (Run) que aparece al lado del método `main`.
5. La consola integrada de IntelliJ mostrará el menú interactivo del programa.

### Opción B: Ejecutar desde la terminal (Maven)
Si prefieres usar la terminal, abre una ventana en la raíz del proyecto (donde está el archivo `pom.xml`) y ejecuta el siguiente comando:

```bash
mvn clean compile exec:java
