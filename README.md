# 🎬 Sistema de Gestión de Películas (H2 & Java)

**Autor:** Robert  
**Tecnologías:** Java 17, H2 Database, Maven, JDBC  
**Arquitectura:** Aplicación de consola (CLI) con base de datos relacional embebida.

---

## 📖 1. Resumen del Proyecto
Este proyecto consiste en una aplicación de gestión por consola diseñada para administrar un catálogo de películas. A diferencia de un CRUD básico, el sistema implementa un modelo relacional complejo de **5 tablas** que permite asociar películas con sus directores, géneros y actores.

El objetivo principal es demostrar el dominio de la conexión a bases de datos mediante JDBC, la ejecución de consultas SQL avanzadas (JOINs y agregaciones) y la aplicación de buenas prácticas de programación.

---

## 📂 2. Estructura del Proyecto (Diagrama de Árbol)

El proyecto sigue la estructura estándar de Maven. Este es el árbol de directorios y archivos principales:

```text
ProyectoBB/
│
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── ejemplo/
│                   ├── DatabaseManager.java   # Clase de conexión y creación de BD
│                   └── GestionPeliculas.java  # Menú principal y lógica de la app
│
├── schema.sql                                 # Script SQL con la estructura de las tablas
├── data.sql                                   # Script SQL con los datos de prueba
├── pom.xml                                    # Configuración de Maven y dependencia H2
├── README.md                                  # Documentación del proyecto
└── peliculasDB.mv.db                          # Archivo de BD (generado automáticamente al ejecutar)
```
🏗️ 3. Descomposición del Programa
El código está dividido en dos clases principales para separar responsabilidades:

A. DatabaseManager.java (Capa de Datos)
Conexión: El método getConnection() gestiona la conexión JDBC con H2 (jdbc:h2:./peliculasDB).

Inicialización y Test de Conexión: El método init() se ejecuta al arrancar la aplicación. Este método actúa como test automático: si la conexión falla, lanza una excepción; si funciona, crea las tablas e inserta los datos por defecto. Si la base de datos ya existe, simplemente conecta.

B. GestionPeliculas.java (Capa de Presentación)
Interfaz: Dibuja un menú interactivo por consola con colores ANSI.

Lógica: Controla el flujo con un do-while y un switch, gestionando las consultas SQL, los errores y la interacción con el usuario.

📊 4. Modelo de Datos (Esquema y Diagrama)
Diagrama Entidad-Relación (ER)
El siguiente diagrama muestra cómo se conectan las 5 tablas a través de sus Primary Keys (PK) y Foreign Keys (FK):

erDiagram
    GENERO ||--o{ PELICULA : "tiene"
    DIRECTOR ||--o{ PELICULA : "dirige"
    PELICULA ||--o{ PELICULA_ACTOR : "participa"
    ACTOR ||--o{ PELICULA_ACTOR : "actúa"

    GENERO {
        int id_genero PK
        string nombre
    }
    DIRECTOR {
        int id_director PK
        string nombre
    }
    ACTOR {
        int id_actor PK
        string nombre
    }
    PELICULA {
        int id_pelicula PK
        string titulo
        int anio
        int id_genero FK
        int id_director FK
    }
    PELICULA_ACTOR {
        int id_pelicula PK, FK
        int id_actor PK, FK
    }
⭐ Uso de Primary Keys (PK) y Foreign Keys (FK)
Para garantizar la integridad de los datos y optar al Notable, el esquema se ha diseñado relacionalmente:

Primary Keys (PK): Identifican de forma única cada fila. En las tablas de catálogo se usa AUTO_INCREMENT para que el ID se genere solo. En la tabla intermedia se usa una PK Compuesta.

Foreign Keys (FK): Crean las relaciones. Impiden, por ejemplo, que se asigne un director a una película si ese director no está registrado en la tabla director.

Estructura de las Tablas (Columnas y Tipos de Datos)
1. Tabla genero

id_genero (INT, PK, Auto-increment)

nombre (VARCHAR(50), UNIQUE)

2. Tabla director

id_director (INT, PK, Auto-increment)

nombre (VARCHAR(100))

3. Tabla actor

id_actor (INT, PK, Auto-increment)

nombre (VARCHAR(100))

4. Tabla pelicula

id_pelicula (INT, PK, Auto-increment)

titulo (VARCHAR(150))

anio (INT)

id_genero (INT, FK → genero.id_genero)

id_director (INT, FK → director.id_director)

5. Tabla pelicula_actor (Tabla Intermedia N:M)

id_pelicula (INT, PK Compuesta, FK → pelicula.id_pelicula)

id_actor (INT, PK Compuesta, FK → actor.id_actor)

📝 5. Pseudocódigo del Funcionamiento
text
ALGORITMO GestionPeliculas
    INICIALIZAR BaseDeDatos (Conectar, crear tablas y datos)
    VARIABLE opcion = -1

    HACER
        LIMPIAR PANTALLA
        IMPRIMIR Menú de opciones (1 al 6, 0 para salir)
        LEER opcion

        INTENTAR
            SEGÚN opcion HACER:
                CASO 1: EjecutarConsultaSimple()
                CASO 2: EjecutarConsultaJOIN()
                CASO 3: EjecutarConsultaAgregacion()
                CASO 4: FlujoInsertarPelicula() 
                CASO 5: FlujoBorrarPelicula()   
                CASO 6: FlujoBuscarPelicula()   
                CASO 0: Imprimir "Saliendo..."
                DEFECTO: Imprimir "Opción no válida"
            
            PAUSAR (Esperar tecla ENTER)
            
        CAPTURAR ExcepcionSQL:
            Imprimir error controlado
            
    MIENTRAS opcion SEA DISTINTO DE 0
FIN ALGORITMO
🔌 6. Prueba de Conexión y Ejecución
🛠️ Requisitos previos
Java Development Kit (JDK) 17 o superior.

Maven instalado (o el integrado en IntelliJ IDEA).

▶️ Ejecutar y probar la conexión (IntelliJ IDEA)
Abre el proyecto en IntelliJ IDEA.

Espera a que Maven descargue la dependencia de H2 (h2-2.2.224.jar en External Libraries).

Abre src/main/java/com/ejemplo/GestionPeliculas.java.

Haz clic en el botón verde ▶ al lado de public static void main.

Prueba de conexión: En la consola verás el mensaje 🌱 Base de datos inicializada con 5 tablas. Si ves esto seguido del menú, la conexión a H2 es exitosa. Si hubiera un fallo, el programa mostraría un mensaje de error en rojo.

💻 Ejecutar desde la terminal (Maven)
Abre una consola en la raíz del proyecto y ejecuta:

bash
mvn clean compile exec:java
🔍 Visualización de la Base de Datos (Consola Web H2)
Si quieres ver las tablas visualmente sin usar Java:

Descarga H2 desde h2database.com y ejecuta h2.bat.

En http://localhost:8082, configura:

Driver Class: org.h2.Driver

JDBC URL: jdbc:h2:C:/Ruta/A/Tu/Proyecto/peliculasDB (Usa barras normales /)

User Name: sa

Password: (en blanco)

⚙️ 7. Funcionalidades (Consultas Implementadas)
Listar (Simple): SELECT * FROM pelicula.

Ver reparto (JOIN): Cruza 4 tablas (pelicula, director, genero, actor) usando INNER JOIN.

Estadísticas (Agregación): Usa COUNT y GROUP BY para contar películas por género.

Añadir: Inserción segura con PreparedStatement y submenús para crear Géneros, Directores o Actores si no existen.

Borrar: Elimina respetando la integridad referencial (borra primero la relación N:M y luego la película).

Buscador (LIKE): Filtro dinámico (insensible a mayúsculas) por Título, Actor o Director.