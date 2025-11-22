package com.riwi.EventAndVenue.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para la documentación de la API.
 * Define información general, contacto, licencia y servidores.
 *
 * @author Juan - RIWI
 * @version 1.0
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Catálogo de Eventos API",
                version = "1.0.0",
                description = "API REST para la gestión de eventos y venues de una tiquetera online. " +
                        "Permite crear, consultar, actualizar y eliminar eventos y venues. " +
                        "Los datos se almacenan en memoria durante la ejecución de la aplicación.",
                contact = @Contact(
                        name = "Juan - RIWI",
                        email = "yepescan1997@gmail.com",
                        url = "https://riwi.io"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "Servidor de Desarrollo",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "Servidor de Pruebas",
                        url = "http://localhost:8081"
                )
        }
)
public class OpenApiConfig {
    // La configuración se realiza mediante anotaciones
    // No requiere beans adicionales con SpringDoc 2.x
}
