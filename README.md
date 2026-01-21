# Tool Rental - Aplicación Spring Boot

Una aplicación simple para la gestión de un servicio de alquiler de herramientas. Proporciona:
- API REST para operaciones sobre Clientes, Herramientas y Alquileres (usando DTOs para los payloads).
- Interfaz web con Thymeleaf para realizar CRUD y operaciones de negocio (crear/cancelar/devolver alquileres, calcular coste, listar disponibles, informes).
- Capa de persistencia con Spring Data JPA.

---

## Resumen / Qué se puede hacer

Desde la API o la interfaz web se pueden realizar:

- CRUD de Clientes (crear, listar, obtener, actualizar, eliminar).
- CRUD de Herramientas.
- Crear Alquiler.
- Cancelar un alquiler.
- Finalizar / devolver un alquiler (calcular multas y montos devueltos).
- Calcular coste de alquiler por herramienta y rango de fechas.
- Listar herramientas disponibles para un rango de fechas.
- Ver alquileres de un cliente.
- Informes:
  - Ingresos por herramienta.
  - Herramientas con stock bajo (umbral configurable).

---

## Tecnologías y herramientas utilizadas

- Java (Spring Boot)
- Spring MVC (controladores REST y Web)
- Spring Data JPA (repositorios)
- Hibernate (JPA provider)
- Thymeleaf (plantillas web)
- Jakarta Validation (validación en DTOs)
- DTOs para evitar exponer directamente entidades JPA
- (Opcional) MapStruct si se desea reemplazar los mappers manuales
- Build: Maven (o Gradle si lo prefieres)
- Base de datos: configurable vía `application.properties` (por ejemplo H2 para desarrollo)

---

## Estructura relevante

- src/main/java/tool_rental/controller — controladores REST (API)
- src/main/java/tool_rental/web — controladores Web (Thymeleaf)
- src/main/java/tool_rental/dto — DTOs usados por la API y el frontend
- src/main/java/tool_rental/mapper — mappers manuales (EntityDtoMapper)
- src/main/java/tool_rental/service — lógica de negocio
- src/main/java/tool_rental/repository — repositorios JPA
- src/main/resources/templates — plantillas Thymeleaf
- src/main/resources/static — assets (CSS, JS)

---

## DTOs principales

- ClienteDTO
  - id, nombre, email, telefono
- HerramientaDTO
  - id, nombre, categoria, precioDia, stock
- AlquilerCreateDTO
  - clienteId, herramientaId, fechaIni, fechaFin, (fianza opcional)
- AlquilerResponseDTO
  - id, clienteId, herramientaId, estado, fechaIni, fechaFin, monto, fianza, multa, montoRetenido, montoDevuelto

Los DTOs se validan usando anotaciones de Jakarta Validation (`@NotNull`, `@NotBlank`, `@Email`, etc.).

---

## Endpoints de la API REST

Base: `http://localhost:8080/api`

Clientes
- GET `/clientes`  
  - Listar todos los clientes  
  - Respuesta: List<ClienteDTO>
- GET `/clientes/{id}`  
  - Obtener cliente por id  
  - Respuesta: ClienteDTO
- POST `/clientes`  
  - Crear cliente (body: ClienteDTO, sin id)  
  - Respuesta: ClienteDTO (creado)
- PUT `/clientes/{id}`  
  - Actualizar cliente (body: ClienteDTO)  
  - Respuesta: ClienteDTO (actualizado)
- DELETE `/clientes/{id}`  
  - Eliminar cliente

Herramientas
- GET `/herramientas`  
  - Listar herramientas — Respuesta: List<HerramientaDTO>
- GET `/herramientas/{id}`  
  - Obtener herramienta por id — Respuesta: HerramientaDTO
- POST `/herramientas`  
  - Crear herramienta (body: HerramientaDTO) — Respuesta: HerramientaDTO
- PUT `/herramientas/{id}`  
  - Actualizar herramienta (body: HerramientaDTO) — Respuesta: HerramientaDTO
- DELETE `/herramientas/{id}`

Alquileres
- GET `/alquileres`  
  - Listar todos los alquileres — Respuesta: List<AlquilerResponseDTO>
- GET `/alquileres/{id}`  
  - Obtener alquiler por id — Respuesta: AlquilerResponseDTO
- GET `/alquileres/cliente/{clienteId}`  
  - Listar alquileres de un cliente — Respuesta: List<AlquilerResponseDTO>
- POST `/alquileres`  
  - Crear alquiler (body: AlquilerCreateDTO) — Respuesta: AlquilerResponseDTO  
    - Campos: clienteId, herramientaId, fechaIni (YYYY-MM-DD), fechaFin (YYYY-MM-DD), fianza (opcional)
- PUT `/alquileres/{id}/estado?estado=VALOR`  
  - Actualizar estado (p. ej. PENDIENTE, EN_CURSO, FINALIZADO, CANCELADO) — Respuesta: AlquilerResponseDTO
- POST `/alquileres/{id}/finalizar`  
  - Finaliza/devolución. Calcula multa y monto devuelto según reglas del servicio.
- POST `/alquileres/{id}/cancelar`  
  - Cancela un alquiler (calcula retenciones según tiempo antes del inicio)

Notas:
- Las reglas de negocio (disponibilidad por fechas, cálculo de multas/retenciones, validaciones) se encuentran en los servicios (`AlquilerService`, `HerramientaService`, `ClienteService`).
- El endpoint de creación de alquiler recibe DTO y el backend mapea a entidad (se crean referencias mínimas con IDs, los servicios validan/recargan entidades completas).

Ejemplos CURL
- Crear cliente:
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Juan Perez","email":"juan@example.com","telefono":"600111222"}'
```

- Crear herramienta:
```bash
curl -X POST http://localhost:8080/api/herramientas \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Taladro","categoria":"Electricas","precioDia":12.50,"stock":3}'
```

- Crear alquiler:
```bash
curl -X POST http://localhost:8080/api/alquileres \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"herramientaId":2,"fechaIni":"2026-02-01","fechaFin":"2026-02-03","fianza":50.0}'
```

---

## Páginas web (Thymeleaf)

Base web: `http://localhost:8080/web`

Rutas principales del frontend:
- `/web` — Panel principal / menú
- `/web/clientes` — Listado de clientes
- `/web/clientes/nuevo` — Crear cliente
- `/web/clientes/{id}/editar` — Editar cliente
- `/web/herramientas` — Listado de herramientas
- `/web/herramientas/nuevo` — Crear herramienta
- `/web/herramientas/{id}/editar` — Editar herramienta
- `/web/alquileres` — Listado de alquileres (acciones: cancelar, finalizar)
- `/web/alquileres/nuevo` — Formulario crear alquiler (usa DTO AlquilerCreateDTO)
- `/web/alquileres/disponibles` — Buscar herramientas disponibles por rango de fechas
- `/web/alquileres/coste` — Calcular coste de alquiler para herramienta y fechas (GET con params)
- `/web/alquileres/cliente/{clienteId}` — Ver alquileres de un cliente
- `/web/alquileres/informes/ingresos` — Informe: ingresos por herramienta
- `/web/alquileres/informes/stock-bajo` — Informe: stock bajo (umbral configurable)

La UI usa los DTOs para el binding de formularios y luego mapea a entidades en el backend antes de invocar los servicios.

---

## Requisitos y cómo ejecutar

Requisitos:
- JDK 17+ (o la versión que uses en el proyecto)
- Maven (o usar el wrapper `./mvnw` si existe)
- Configurar la conexión a la base de datos en `src/main/resources/application.properties` (por ejemplo H2 para desarrollo)

Ejecutar en modo desarrollo:
```bash
# con Maven
mvn spring-boot:run

# o con el wrapper (si existe)
./mvnw spring-boot:run
```

Luego abrir en:
- API: `http://localhost:8080/api/...`
- Web UI: `http://localhost:8080/web`

---
