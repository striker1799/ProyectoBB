# 🎬 Gestión de Películas – Aplicación H2

Pequeña aplicación de gestión de películas que utiliza una base de datos **H2** con 3 tablas relacionadas. Incluye un menú interactivo por consola y las consultas SQL requeridas (simple, JOIN y agregación).

## 📋 Modelo de datos

| Tabla | Claves | Columnas |
|-------|--------|----------|
| **genero**   | PK `id_genero` | `nombre` |
| **director** | PK `id_director` | `nombre` |
| **pelicula** | PK `id_pelicula`, FK `id_genero` → genero, FK `id_director` → director | `titulo`, `anio` |

## 🔎 Consultas SQL

**1. Simple:**
```sql
SELECT * FROM pelicula;
```

**2. JOIN:**
```sql
SELECT p.titulo, d.nombre AS director, g.nombre AS genero
FROM pelicula p
INNER JOIN director d ON p.id_director = d.id_director
INNER JOIN genero   g ON p.id_genero   = g.id_genero;
```

**3. Agregación:**
```sql
SELECT g.nombre AS genero, COUNT(p.id_pelicula) AS total
FROM genero g
LEFT JOIN pelicula p ON g.id_genero = p.id_genero
GROUP BY g.nombre
ORDER BY total DESC;
```

## 🖥️ Menú interactivo

| Opción | Acción | Tipo |
|--------|--------|------|
| 1 | Listar películas | Consulta simple |
| 2 | Ver películas con JOIN | JOIN |
| 3 | Contar películas por género | Agregación |
| 4 | Añadir género | Inserción |
| 5 | Añadir director | Inserción |
| 6 | Añadir película | Inserción |
| 7 | Eliminar película por ID | Borrado |
| 0 | Salir | — |

## 🚀 Cómo ejecutar

Requisitos: **Java 17+** y **Maven**.

```bash
mvn clean compile exec:java
```

La primera vez que se ejecuta, `DatabaseManager.init()` crea las tablas e inserta los datos de ejemplo. La base de datos se guarda en `./peliculasDB.mv.db`.

## 🛠️ Tecnologías

- H2 Database 2.x
- Java 17 (JDBC)
- Maven

## 👤 Autor

- **Nombre**: Tu Nombre
- **Curso**: Desarrollo de Aplicaciones / Programación
- **Repositorio**: https://github.com/striker1799/ProyectoBB