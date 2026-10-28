# 🎬 Sistema de Gestión de Películas (Java + H2)

**Autor:** Robert
**Proyecto:** Aplicación de gestión de base de datos relacional

## 📖 Resumen del Proyecto
Este proyecto es una aplicación de consola desarrollada en **Java** que se conecta a una base de datos relacional **H2**. El sistema permite administrar un catálogo de películas, gestionando de forma relacional los géneros, directores y actores.

El objetivo principal de la práctica es demostrar el dominio del modelado relacional, el uso de JDBC para la conexión a bases de datos, la ejecución de consultas SQL complejas y el control de versiones profesional con Git.

## 🛠️ Tecnologías Utilizadas
*   **Lenguaje:** Java (JDK 17 o superior)
*   **Gestor de dependencias:** Maven
*   **Base de Datos:** H2 (Modo archivo, versión 2.2.224)
*   **Entorno de Desarrollo:** IntelliJ IDEA

---

## 📊 Modelo de Datos (Esquema)

El proyecto utiliza un modelo relacional de **5 tablas**. Se han definido correctamente las Claves Primarias (PK) y las Claves Foráneas (FK) para garantizar la integridad referencial, demostrando relaciones **Uno a Muchos (1:N)** y **Muchos a Muchos (N:M)**.

| Tabla | Tipo | Claves y Restricciones |
| :--- | :--- | :--- |
| **`genero`** | Catálogo | **PK:** `id_genero` |
| **`director`** | Catálogo | **PK:** `id_director` |
| **`actor`** | Catálogo | **PK:** `id_actor` |
| **`pelicula`** | Principal | **PK:** `id_pelicula`<br>**FK:** `id_genero` (→ genero)<br>**FK:** `id_director` (→ director) |
| **`pelicula_actor`** | Intermedia (N:M) | **PK Compuesta:** `id_pelicula` + `id_actor`<br>**FK:** `id_pelicula` (→ pelicula)<br>**FK:** `id_actor` (→ actor) |

*Los scripts de creación de tablas e inserción de datos se encuentran en el archivo `schema.sql` (y también están integrados en la clase `DatabaseManager.java` para su creación automática).*

---

## 📝 Pseudocódigo del Funcionamiento

Lógica general del flujo de la aplicación por consola:

    INICIO
        Inicializar Base de Datos (Crear tablas y datos si no existen)
        
        HACER
            Limpiar pantalla
            Mostrar Menú de Opciones (1 al 6, o 0 para salir)
            Pedir opción al usuario
            
            SEGÚN opción:
                Caso 1: Ejecutar SELECT simple (Listar películas)
                Caso 2: Ejecutar SELECT con JOIN (Ver reparto cruzando 4 tablas)
                Caso 3: Ejecutar SELECT con GROUP BY (Estadísticas por género)
                Caso 4: Pedir datos -> INSERT (Añadir película de forma guiada)
                Caso 5: Pedir ID -> DELETE (Borrar película y sus relaciones)
                Caso 6: Pedir texto -> SELECT con LIKE (Buscador por título/actor/director)
                Caso 0: Salir del bucle
                
            Pausar hasta que el usuario pulse ENTER
            
        MIENTRAS opción sea distinta de 0
    FIN

---

## 💻 Instalación de H2 (Windows)

Aunque el proyecto incluye la dependencia de H2 a través de Maven (por lo que el código compilará y funcionará sin instalar nada extra), si deseas inspeccionar la base de datos visualmente, puedes instalar la consola web de H2:

1.  Ve a la página oficial: [https://www.h2database.com/html/download.html](https://www.h2database.com/html/download.html).
2.  Descarga el **Windows Installer** (o el archivo `.zip` portable).
3.  Si usas el instalador, ejecútalo y sigue los pasos. Si usas el `.zip`, descomprímelo y ejecuta el archivo `h2.bat` de la carpeta `bin`.
4.  Se abrirá una consola de comandos y tu navegador se dirigirá a `http://localhost:8082`.
5.  **Para conectarte a los datos del proyecto:**
    *   **JDBC URL:** `jdbc:h2:C:/Users/Struker/Documents/ProyectoBB/peliculasDB` *(Asegúrate de que la ruta es la correcta a tu archivo).*
    *   **User Name:** `sa`
    *   **Password:** *(dejar en blanco)*

---

## 🚀 Cómo Ejecutar el Proyecto

### Requisitos previos
Tener instalado **Java (JDK 17+)** y **Maven**.

### Opción A: Desde la terminal (Maven)
Abre una terminal en la raíz del proyecto (donde está el `pom.xml`) y ejecuta:
```bash
mvn clean compile exec:java

Opción B: Desde IntelliJ IDEA (Recomendado)
Abre IntelliJ IDEA y carga el proyecto (File → Open).

Espera a que Maven sincronice las dependencias (verás la librería de H2 en External Libraries).

Abre el archivo src/main/java/com/ejemplo/GestionPeliculas.java.

Haz clic en el botón verde ▶ al lado del método public static void main.

⚙️ Funcionalidades Implementadas (Consultas SQL)
El menú interactivo cuenta con las siguientes opciones que demuestran el uso de diferentes consultas:

Listar películas (Simple): Uso de SELECT * FROM pelicula.

Ver reparto (JOIN): Uso de INNER JOIN para cruzar las tablas pelicula, director, genero y actor. Demuestra la capacidad de unir datos relacionales.

Estadísticas (Agregación): Uso de COUNT y GROUP BY para contar cuántas películas hay registradas por cada género.

Añadir película: Inserta datos en pelicula y pelicula_actor usando PreparedStatement (para evitar inyección SQL). Permite crear géneros, directores o actores nuevos sobre la marcha si no existen.

Borrar película: Elimina registros respetando la integridad referencial (borra primero el reparto en la tabla intermedia y luego la película).

Buscador (LIKE): Búsqueda dinámica e insensible a mayúsculas usando el operador LIKE de SQL para encontrar películas por título, actor o director.