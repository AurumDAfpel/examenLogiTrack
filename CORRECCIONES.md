# Correcciones aplicadas a LogiTrack

## 1. Rutas de la API no coincidían (causa principal de que no funcionara)
El frontend (`frontend/index.html`) llama a `http://localhost:8081/api/...`,
pero el backend no tenía ningún prefijo `/api` configurado, así que **todas
las peticiones daban 404**.

**Arreglo:** se agregó en `application.properties`:
```
server.servlet.context-path=/api
```

## 2. El `index.html` estaba en una carpeta que Spring Boot no sirve
Estaba en `src/main/resources/schema/index.html`. Spring Boot solo sirve
archivos estáticos desde `static/`, `public/` o `META-INF/resources`.

**Arreglo:** se sacó el frontend a una carpeta independiente `frontend/`
(fuera del backend). Como el `SecurityConfig` ya tiene CORS abierto
(`Access-Control-Allow-Origin: *`), puedes abrir ese `index.html`
directamente en el navegador o servirlo con cualquier servidor estático,
sin que dependa de Spring Boot.

## 3. No existían datos ni el usuario "admin" en la base de datos
`Data.sql`/`schema.sql` estaban en `resources/schema/`, una ruta que Spring
Boot no ejecuta automáticamente. Además `schema.sql` usaba sintaxis de
MySQL (`AUTO_INCREMENT`, `DATETIME`), que **no es válida en PostgreSQL**.
Resultado: nunca se creaba el usuario `admin`, así que el login siempre
fallaba.

Además, el hash bcrypt que traía el `Data.sql` original **no correspondía
realmente a la contraseña `admin123`** (fue puesto ahí a mano y no coincidía
al verificarlo), así que aunque se hubiera insertado, el login habría
seguido fallando con "credenciales inválidas". Se generó y verificó un hash
bcrypt nuevo y correcto para `admin123`.

**Arreglo:**
- Se eliminó el `schema.sql` viejo (Hibernate ya crea las tablas solo, con
  `spring.jpa.hibernate.ddl-auto=update`).
- Se creó `src/main/resources/data.sql` (ubicación correcta) con sintaxis
  válida para PostgreSQL y un hash bcrypt verificado para `admin123`.
- Los INSERT de usuarios usan `ON CONFLICT (username) DO UPDATE`, así que
  si ya habías corrido la app antes y el usuario `admin` quedó guardado con
  el hash incorrecto, **al reiniciar la app se corrige automáticamente**.
- Se agregó en `application.properties`:
  ```
  spring.jpa.defer-datasource-initialization=true
  spring.sql.init.mode=always
  ```
  para que `data.sql` se ejecute después de que Hibernate cree las tablas.

**Usuarios de prueba creados automáticamente:**
| usuario  | contraseña |
|----------|------------|
| admin    | admin123   |
| empleado | admin123   |

## 4. `pom.xml` tenía la dependencia de PostgreSQL duplicada
Se eliminó la declaración repetida.

## 5. Login sin manejo de errores
Cuando las credenciales eran inválidas, la app respondía con un error 500
genérico en vez de un 401 claro. Se agregó un manejador para
`AuthenticationException` que devuelve un JSON limpio con status 401.

## 6. ⚠️ MUY IMPORTANTE — credenciales expuestas
Tu `application.properties` tenía la contraseña real de tu base de datos en
Aiven y el secreto usado para firmar los JWT **escritos en texto plano**, y
es muy probable que ya estén subidos a tu repositorio de GitHub (aunque sea
privado, quedan en el historial de commits).

**Recomendaciones:**
1. Entra a tu panel de Aiven y **rota (cambia) la contraseña de la base de
   datos ahora mismo**.
2. Genera un nuevo secreto para `app.jwt.secret` (una cadena aleatoria larga).
3. Ahora esos valores se leen como variables de entorno con un valor por
   defecto:
   ```
   spring.datasource.password=${DB_PASSWORD:valor_por_defecto}
   app.jwt.secret=${JWT_SECRET:valor_por_defecto}
   ```
   Para producción, define `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` y
   `JWT_SECRET` como variables de entorno reales y **no los dejes** en el
   archivo (agrégalo a `.gitignore` o usa un `application-local.properties`
   que no subas al repo).

## 7. Registro de usuarios restringido a administradores
Antes, `POST /auth/register` era público: cualquiera sin sesión podía
crearse una cuenta (incluso con rol `ROLE_ADMIN`). Se eliminó ese endpoint
público y se movió la creación de usuarios a `POST /usuarios`, que ya está
protegido por `SecurityConfig` (`hasRole("ADMIN")`). Ahora solo un
administrador autenticado puede crear usuarios nuevos, y se valida que el
rol sea `ROLE_ADMIN` o `ROLE_EMPLEADO` y que la contraseña tenga al menos 6
caracteres. El único endpoint público en `/auth` que queda es `/login`.

## Cómo correrlo

Requisitos: Java 17+ (tienes JDK 21, funciona) y conexión a tu base de datos
PostgreSQL.

```bash
# desde la carpeta logitrack/
./mvnw spring-boot:run
```

El backend queda en `http://localhost:8081/api`.

Luego abre `frontend/index.html` directamente en tu navegador (doble clic,
o `python3 -m http.server` desde esa carpeta) e inicia sesión con
`admin` / `admin123`.
