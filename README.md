# 🎭 Events and Venues API

Sistema completo de gestión de eventos y venues (locaciones) con autenticación JWT, validaciones avanzadas, logging estructurado, métricas de monitoreo y despliegue con Docker.

## 📋 Tabla de Contenidos

- [Descripción](#descripción)
- [Tecnologías](#tecnologías)
- [Arquitectura](#arquitectura)
- [Prerequisitos](#prerequisitos)
- [Instalación](#instalación)
- [Ejecución Local](#ejecución-local)
- [Ejecución con Docker](#ejecución-con-docker)
- [Usuarios de Prueba](#usuarios-de-prueba)
- [Endpoints Principales](#endpoints-principales)
- [Documentación API (Swagger)](#documentación-api-swagger)
- [Testing](#testing)
- [Métricas y Monitoreo](#métricas-y-monitoreo)
- [Base de Datos](#base-de-datos)

---

## 📖 Descripción

**Events and Venues API** es una aplicación REST para gestionar eventos y sus locaciones (venues), permitiendo:

- ✅ Crear, actualizar, consultar y eliminar eventos
- ✅ Crear, actualizar, consultar y eliminar venues
- ✅ Búsquedas y filtros dinámicos
- ✅ Autenticación y autorización con JWT
- ✅ Control de acceso basado en roles (USER, ORGANIZER, ADMIN)
- ✅ Validaciones avanzadas con Bean Validation
- ✅ Logging estructurado con trazabilidad (traceId)
- ✅ Métricas de negocio y monitoreo con Actuator
- ✅ Migraciones de base de datos con Flyway
- ✅ Despliegue con Docker y Docker Compose

---

## 🛠️ Tecnologías

- **Java 21** - Lenguaje de programación
- **Spring Boot 3.5.7** - Framework principal
- **Spring Security 6** - Autenticación y autorización
- **JWT (JSON Web Tokens)** - Tokens de autenticación
- **Spring Data JPA** - Persistencia de datos
- **Hibernate** - ORM
- **Flyway** - Migraciones de base de datos
- **H2 Database** - Base de datos en memoria (desarrollo)
- **MySQL 8.0** - Base de datos relacional (producción)
- **Actuator + Micrometer** - Métricas y monitoreo
- **Logback + Logstash Encoder** - Logging estructurado
- **Bean Validation** - Validaciones
- **MapStruct** - Mapeo de objetos
- **Swagger/OpenAPI 3** - Documentación API
- **JUnit 5** - Testing unitario
- **Mockito** - Mocks para testing
- **JaCoCo** - Cobertura de código
- **Docker + Docker Compose** - Containerización
- **Maven** - Gestión de dependencias

---

## 🏗️ Arquitectura

El proyecto sigue **Arquitectura Hexagonal (Ports & Adapters)** con 3 capas principales:
```
📦 com.riwi.EventAndVenue
├── 📂 domain_hexagonal          # Capa de Dominio
│   ├── model/                   # Entidades de dominio (Event, Venue, User)
│   └── ports/                   # Interfaces de puertos (in/out)
├── 📂 application_hexagonal     # Capa de Aplicación
│   ├── usecase/                 # Casos de uso (lógica de negocio)
│   └── service/                 # Servicios de aplicación
└── 📂 infrastructure_hexagonal  # Capa de Infraestructura
    ├── adapters/
    │   ├── in.web/              # Controladores REST
    │   └── out.persistence/     # Repositorios JPA
    ├── config/                  # Configuraciones
    ├── security/                # JWT, filtros, providers
    └── exception/               # Manejo de excepciones
```

### Ventajas de esta arquitectura:
- ✅ Independencia de frameworks
- ✅ Testeable (lógica aislada)
- ✅ Mantenible y escalable
- ✅ Bajo acoplamiento

---

## ⚙️ Prerequisitos

### Para ejecución local:
- **Java 21** ([Descargar](https://adoptium.net/))
- **Maven 3.9+** ([Descargar](https://maven.apache.org/download.cgi))
- **IDE** (IntelliJ IDEA recomendado)

### Para ejecución con Docker:
- **Docker Desktop** ([Descargar](https://www.docker.com/products/docker-desktop))
- Docker Compose (incluido en Docker Desktop)

---

## 📥 Instalación

### 1. Clonar el repositorio
```bash
git clone https://github.com/tu-usuario/EventsAndVenues.git
cd EventsAndVenues
```

### 2. Instalar dependencias
```bash
mvn clean install
```

---

## 🚀 Ejecución Local

### Opción 1: Desde IntelliJ IDEA

1. Abrir el proyecto en IntelliJ
2. Esperar a que Maven descargue dependencias
3. Ejecutar la clase `H1Application.java`
4. La aplicación arrancará en: `http://localhost:8080`

### Opción 2: Desde línea de comandos
```bash
mvn spring-boot:run
```

### Base de datos en desarrollo

Por defecto, usa **H2 en memoria**:
- URL: `jdbc:h2:mem:testdb`
- Consola H2: `http://localhost:8080/h2-console`
- Usuario: `sa`
- Password: (vacío)

---

## 🐳 Ejecución con Docker

Para ejecutar con **MySQL en Docker**, ver documentación completa en:

👉 **[README-DOCKER.md](README-DOCKER.md)**

### Inicio rápido:
```bash
# Construir y levantar contenedores
docker-compose up --build

# Aplicación disponible en: http://localhost:8080
# MySQL disponible en: localhost:3307
```

---

## 👥 Usuarios de Prueba

La aplicación incluye usuarios pre-configurados (migración Flyway V6):

### 🔑 Usuario ADMIN
```json
Username: admin
Email: admin@test.com
Password: password123
Rol: ROLE_ADMIN
```

**Permisos:**
- ✅ Todas las operaciones de eventos
- ✅ Todas las operaciones de venues
- ✅ Eliminar cualquier recurso

---

### 🎪 Usuario ORGANIZER
```json
Username: organizer
Email: organizer@test.com
Password: password123
Rol: ROLE_ORGANIZER
```

**Permisos:**
- ✅ Crear, actualizar eventos
- ✅ Crear, actualizar venues
- ❌ No puede eliminar venues

---

### 👤 Usuarios normales (USER)

Puedes registrar nuevos usuarios con rol `ROLE_USER` a través del endpoint `/auth/register`.

**Permisos:**
- ✅ Crear, actualizar eventos
- ❌ No puede gestionar venues
- ❌ No puede eliminar recursos

---

## 🔌 Endpoints Principales

### Autenticación (Público)
```http
POST /auth/register   # Registrar nuevo usuario
POST /auth/login      # Iniciar sesión (obtener JWT)
```

### Eventos
```http
GET    /api/events              # Listar eventos (público)
GET    /api/events/{id}         # Obtener evento por ID (público)
GET    /api/events/search       # Buscar eventos con filtros (público)
POST   /api/events              # Crear evento (autenticado)
PUT    /api/events/{id}         # Actualizar evento (autenticado)
DELETE /api/events/{id}         # Eliminar evento (solo ADMIN)
```

### Venues
```http
GET    /api/venues              # Listar venues (público)
GET    /api/venues/{id}         # Obtener venue por ID (público)
GET    /api/venues/search       # Buscar venues con filtros (público)
POST   /api/venues              # Crear venue (ORGANIZER o ADMIN)
PUT    /api/venues/{id}         # Actualizar venue (ORGANIZER o ADMIN)
DELETE /api/venues/{id}         # Eliminar venue (solo ADMIN)
```

### Métricas y Salud
```http
GET /actuator/health            # Estado de salud de la aplicación
GET /actuator/metrics           # Lista de métricas disponibles
GET /actuator/prometheus        # Métricas en formato Prometheus
```

---

## 📚 Documentación API (Swagger)

### Acceder a Swagger UI:
```
http://localhost:8080/swagger-ui/index.html
```

### Cómo usar Swagger:

#### 1️⃣ Hacer Login

1. Ir a **auth-controller**
2. Expandir `POST /auth/login`
3. Click en **"Try it out"**
4. Usar credenciales de prueba:
```json
{
  "usernameOrEmail": "organizer",
  "password": "password123"
}
```

5. Click en **"Execute"**
6. **Copiar el token** de la respuesta

#### 2️⃣ Autorizar en Swagger

1. Click en el botón **"Authorize"** 🔒 (arriba a la derecha)
2. En el campo **"Value"**, pegar:
```
   Bearer TU_TOKEN_AQUI
```
3. Click en **"Authorize"**
4. Click en **"Close"**

#### 3️⃣ Probar endpoints protegidos

Ahora puedes ejecutar cualquier endpoint que requiera autenticación.

---

## 🧪 Testing

### Ejecutar todos los tests:
```bash
mvn test
```

### Ejecutar tests con reporte de cobertura:
```bash
mvn clean test jacoco:report
```

### Ver reporte de cobertura:

Abrir en navegador:
```
target/site/jacoco/index.html
```

### Estadísticas de Testing:

- **Tests unitarios**: 26 tests
- **Cobertura total**: 22%
- **Cobertura en use cases**: 67%
- **Herramientas**: JUnit 5, Mockito, JaCoCo

---

## 📊 Métricas y Monitoreo

### Métricas disponibles:

#### Métricas de Negocio:
```http
GET /actuator/metrics/events.created       # Eventos creados
GET /actuator/metrics/venues.created       # Venues creados
GET /actuator/metrics/events.deleted       # Eventos eliminados
GET /actuator/metrics/venues.deleted       # Venues eliminados
GET /actuator/metrics/users.registered     # Usuarios registrados
GET /actuator/metrics/login.success        # Logins exitosos
GET /actuator/metrics/login.failure        # Logins fallidos
```

#### Métricas de Performance:
```http
GET /actuator/metrics/events.creation.time  # Tiempo de creación de eventos
GET /actuator/metrics/venues.creation.time  # Tiempo de creación de venues
GET /actuator/metrics/http.server.requests  # Peticiones HTTP
GET /actuator/metrics/jvm.memory.used       # Memoria JVM usada
```

### Ejemplo de respuesta de métrica:
```json
{
  "name": "events.created",
  "description": "Total number of events created",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 15.0
    }
  ],
  "availableTags": [
    {
      "tag": "type",
      "values": ["business"]
    }
  ]
}
```

### Health Check:
```http
GET /actuator/health
```

Respuesta:
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "H2",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP"
    }
  }
}
```

---

## 🗄️ Base de Datos

### Migraciones Flyway:

Las migraciones se ejecutan automáticamente al iniciar la aplicación:

- **V1**: Crear tabla `roles`
- **V2**: Crear tabla `users`
- **V3**: Crear tabla `user_roles` (relación many-to-many)
- **V4**: Insertar roles por defecto (USER, ADMIN, ORGANIZER)
- **V5**: Ajustes de esquema
- **V6**: Insertar usuarios de prueba (admin, organizer)

### Esquema de Base de Datos:
```
┌─────────────┐       ┌──────────────┐       ┌─────────────┐
│   users     │───────│  user_roles  │───────│    roles    │
├─────────────┤       ├──────────────┤       ├─────────────┤
│ id          │       │ user_id (FK) │       │ id          │
│ username    │       │ role_id (FK) │       │ name        │
│ email       │       └──────────────┘       │ description │
│ password    │                              └─────────────┘
│ enabled     │
│ created_at  │
│ updated_at  │
└─────────────┘

┌─────────────┐       ┌─────────────┐
│   events    │       │   venues    │
├─────────────┤       ├─────────────┤
│ id          │       │ id          │
│ name        │       │ name        │
│ description │       │ location    │
│ event_date  │       │ capacity    │
│ venue_id    │──────▶│ description │
│ capacity    │       │ active      │
│ ticket_price│       │ created_at  │
│ active      │       │ updated_at  │
│ created_at  │       └─────────────┘
│ updated_at  │
└─────────────┘
```

---

## 📁 Estructura del Proyecto
```
EventsAndVenues/
├── src/
│   ├── main/
│   │   ├── java/com/riwi/EventAndVenue/
│   │   │   ├── domain_hexagonal/
│   │   │   ├── application_hexagonal/
│   │   │   └── infrastructure_hexagonal/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-docker.properties
│   │       ├── logback-spring.xml
│   │       ├── messages.properties
│   │       └── db/migration/
│   └── test/
│       └── java/com/riwi/EventAndVenue/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
├── README.md
└── README-DOCKER.md
```

---

## 🔒 Seguridad

### Autenticación JWT:

- Tokens firmados con HS512
- Expiración configurable (default: 24 horas)
- Roles incluidos en el token
- Refresh automático en cada petición

### Configuración de Seguridad:

- **Endpoints públicos**: `/auth/**`, `/api/**/search`, `/actuator/**`, `/swagger-ui/**`
- **Endpoints autenticados**: POST, PUT (eventos y venues)
- **Solo ADMIN**: DELETE endpoints

### Variables de Entorno:
```properties
# JWT Configuration
JWT_SECRET=tu-clave-secreta-super-segura-de-al-menos-256-bits-long
JWT_EXPIRATION=86400000
```

---

## 🚨 Troubleshooting

### Error: Puerto 8080 ya en uso
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F

# Linux/Mac
lsof -ti:8080 | xargs kill -9
```

### Error: Flyway migrations fallan
```bash
# Limpiar base de datos H2
rm -rf ~/eventsandvenues.mv.db

# O configurar en application.properties:
spring.jpa.hibernate.ddl-auto=create-drop  # Solo temporalmente
```

### Error: Tests fallan
```bash
# Limpiar y recompilar
mvn clean install -DskipTests
mvn test
```

---

## 📞 Soporte

Para reportar problemas o sugerencias:
- Crear un Issue en GitHub
- Contactar al equipo de desarrollo

---

## 👨‍💻 Autor

**Juan Rico**  
Backend Developer @ RIWI  
Medellín, Colombia

---

## 📄 Licencia

Este proyecto es parte del programa de formación de RIWI.

---

**¡Gracias por usar Events and Venues API!** 🎉