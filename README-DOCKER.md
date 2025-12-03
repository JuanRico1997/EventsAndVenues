# 🐳 Docker Deployment Guide

Este documento explica cómo ejecutar la aplicación Events and Venues usando Docker y Docker Compose.

## 📋 Prerequisitos

- Docker Desktop instalado ([Descargar aquí](https://www.docker.com/products/docker-desktop))
- Docker Compose incluido en Docker Desktop

## 🚀 Instalación de Docker en Windows

1. Descargar Docker Desktop desde: https://www.docker.com/products/docker-desktop
2. Instalar el ejecutable
3. Reiniciar el sistema
4. Abrir Docker Desktop
5. Verificar instalación en PowerShell:
```bash
   docker --version
   docker-compose --version
```

## 📦 Arquitectura de Contenedores

La aplicación usa dos contenedores:

- **mysql**: Base de datos MySQL 8.0
- **app**: Aplicación Spring Boot

Ambos contenedores se comunican a través de una red Docker privada (`app-network`).

## ⚙️ Configuración

### Variables de Entorno (docker-compose.yml)

**MySQL:**
- `MYSQL_ROOT_PASSWORD`: rootpassword
- `MYSQL_DATABASE`: eventsandvenues_db
- `MYSQL_USER`: eventsuser
- `MYSQL_PASSWORD`: eventspassword

**Spring Boot:**
- `SPRING_DATASOURCE_URL`: jdbc:mysql://mysql:3306/eventsandvenues_db
- `SPRING_DATASOURCE_USERNAME`: eventsuser
- `SPRING_DATASOURCE_PASSWORD`: eventspassword
- `SPRING_JPA_HIBERNATE_DDL_AUTO`: validate
- `SPRING_FLYWAY_ENABLED`: true

### Puertos Expuestos

- **MySQL**: `3307:3306` (puerto 3307 en host → 3306 en contenedor)
- **App**: `8080:8080` (puerto 8080 en host → 8080 en contenedor)

## 🏃 Cómo Ejecutar

### 1. Construir y levantar contenedores

Desde la raíz del proyecto:
```bash
docker-compose up --build
```

**Primera ejecución:** Tardará 5-10 minutos (descarga de imágenes base, build de la app)

**Ejecuciones siguientes:** Tardará 2-3 minutos

### 2. Verificar que todo esté funcionando

Cuando veas en la consola:
```
eventsandvenues-app | Started H1Application in X.XXX seconds
```

La aplicación está lista.

### 3. Acceder a la aplicación

- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **Actuator Health**: http://localhost:8080/actuator/health
- **API Base**: http://localhost:8080/api

### 4. Usuarios de prueba

La aplicación incluye usuarios pre-configurados (migración V6):

**Admin:**
- Username: `admin`
- Email: `admin@test.com`
- Password: `password123`
- Rol: `ROLE_ADMIN`

**Organizer:**
- Username: `organizer`
- Email: `organizer@test.com`
- Password: `password123`
- Rol: `ROLE_ORGANIZER`

## 🛑 Detener los Contenedores

### Detener sin eliminar datos:
```bash
docker-compose down
```

### Detener y eliminar volúmenes (limpieza completa):
```bash
docker-compose down -v
```

## 🔄 Reconstruir la Aplicación

Si modificas el código fuente:
```bash
docker-compose up --build
```

## 📊 Comandos Útiles

### Ver contenedores en ejecución:
```bash
docker ps
```

### Ver logs de la aplicación:
```bash
docker-compose logs -f app
```

### Ver logs de MySQL:
```bash
docker-compose logs -f mysql
```

### Acceder al contenedor de la app:
```bash
docker exec -it eventsandvenues-app sh
```

### Acceder a MySQL:
```bash
docker exec -it eventsandvenues-mysql mysql -u eventsuser -peventspassword eventsandvenues_db
```

## 🗄️ Persistencia de Datos

Los datos de MySQL se persisten en un volumen Docker llamado `mysql-data`.

Esto significa que:
- ✅ Los datos sobreviven a `docker-compose down`
- ❌ Los datos se eliminan con `docker-compose down -v`

## 🐛 Troubleshooting

### Error: "port 3307 is already allocated"

Otro servicio está usando el puerto 3307.

**Solución:** Cambiar el puerto en `docker-compose.yml`:
```yaml
ports:
  - "3308:3306"  # Cambiar 3307 a 3308
```

### Error: "port 8080 is already allocated"

Tienes la aplicación corriendo localmente en IntelliJ.

**Solución:** Detén la aplicación local antes de ejecutar Docker.

### La aplicación no conecta a MySQL

**Verificar que MySQL esté healthy:**
```bash
docker ps
```

Debe mostrar `healthy` en la columna STATUS para mysql.

Si no, espera 30 segundos más para que MySQL termine de iniciar.

### Limpiar todo y empezar de nuevo
```bash
docker-compose down -v
docker-compose up --build
```

## 📁 Estructura de Archivos Docker
```
EventsAndVenues/
├── Dockerfile                      # Imagen de la aplicación
├── docker-compose.yml              # Orquestación de contenedores
├── src/main/resources/
│   ├── application.properties      # Config para desarrollo local (H2)
│   └── application-docker.properties  # Config para Docker (MySQL)
└── README-DOCKER.md               # Esta guía
```

## 🔐 Seguridad en Producción

⚠️ **IMPORTANTE**: Las credenciales en `docker-compose.yml` son para desarrollo local.

En producción:
1. Usar variables de entorno reales
2. Usar Docker Secrets o herramientas como HashiCorp Vault
3. NO commitear credenciales reales al repositorio
4. Cambiar todas las contraseñas por defecto

## 📝 Notas Adicionales

- **Perfil de Spring**: La app automáticamente detecta MySQL y usa el perfil `docker`
- **Flyway**: Las migraciones se ejecutan automáticamente al iniciar
- **Health Check**: MySQL tiene un health check que verifica que esté listo antes de iniciar la app
- **Red interna**: Los contenedores se comunican por nombres (`mysql`, `app`) gracias a la red `app-network`

## 🎯 Próximos Pasos

Para deploy en producción, considera:
- Usar imágenes multi-stage para reducir tamaño
- Implementar health checks en la app
- Configurar restart policies
- Usar orquestadores como Kubernetes o Docker Swarm
- Implementar CI/CD con GitHub Actions o Jenkins