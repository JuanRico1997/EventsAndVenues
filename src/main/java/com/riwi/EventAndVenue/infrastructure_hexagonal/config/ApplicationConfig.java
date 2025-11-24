package com.riwi.EventAndVenue.infrastructure_hexagonal.config;

import com.riwi.EventAndVenue.application_hexagonal.service.EventQueryService;
import com.riwi.EventAndVenue.application_hexagonal.service.VenueQueryService;
import com.riwi.EventAndVenue.application_hexagonal.usecase.*;
import com.riwi.EventAndVenue.domain_hexagonal.ports.in.*;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.VenueRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Spring para la aplicación hexagonal.
 *
 * Esta clase registra todos los casos de uso y servicios como beans de Spring.
 * Esto permite la inyección de dependencias sin contaminar la capa de aplicación
 * con anotaciones de Spring.
 *
 * ¿Por qué aquí y no con @Service en los casos de uso?
 * - Mantiene la capa de aplicación independiente de frameworks
 * - Los casos de uso no conocen a Spring
 * - Facilita testing y cambio de framework en el futuro
 */
@Configuration
public class ApplicationConfig {

    // ========================================
    // CASOS DE USO DE EVENT
    // ========================================

    /**
     * Bean para crear eventos.
     * Spring inyecta automáticamente los repositorios necesarios.
     */
    @Bean
    public CreateEventUseCase createEventUseCase(EventRepositoryPort eventRepository,
                                                 VenueRepositoryPort venueRepository) {
        return new CreateEventUseCaseImpl(eventRepository, venueRepository);
    }

    /**
     * Bean para actualizar eventos.
     */
    @Bean
    public UpdateEventUseCase updateEventUseCase(EventRepositoryPort eventRepository,
                                                 VenueRepositoryPort venueRepository) {
        return new UpdateEventUseCaseImpl(eventRepository, venueRepository);
    }

    /**
     * Bean para eliminar eventos.
     */
    @Bean
    public DeleteEventUseCase deleteEventUseCase(EventRepositoryPort eventRepository) {
        return new DeleteEventUseCaseImpl(eventRepository);
    }

    /**
     * Bean para consultas de eventos.
     */
    @Bean
    public EventQueryService eventQueryService(EventRepositoryPort eventRepository) {
        return new EventQueryService(eventRepository);
    }

    // ========================================
    // CASOS DE USO DE VENUE
    // ========================================

    /**
     * Bean para crear venues.
     */
    @Bean
    public CreateVenueUseCase createVenueUseCase(VenueRepositoryPort venueRepository) {
        return new CreateVenueUseCaseImpl(venueRepository);
    }

    /**
     * Bean para actualizar venues.
     */
    @Bean
    public UpdateVenueUseCase updateVenueUseCase(VenueRepositoryPort venueRepository) {
        return new UpdateVenueUseCaseImpl(venueRepository);
    }

    /**
     * Bean para eliminar venues.
     */
    @Bean
    public DeleteVenueUseCase deleteVenueUseCase(VenueRepositoryPort venueRepository,
                                                 EventRepositoryPort eventRepository) {
        return new DeleteVenueUseCaseImpl(venueRepository, eventRepository);
    }

    /**
     * Bean para consultas de venues.
     */
    @Bean
    public VenueQueryService venueQueryService(VenueRepositoryPort venueRepository) {
        return new VenueQueryService(venueRepository);
    }
}
/**



        ## 🎓 **Explicación del código:**

        ### **@Configuration**
        - Indica que es una clase de configuración de Spring
        - Spring procesa todos los métodos @Bean

        ### **@Bean**
        - Cada método crea y retorna una instancia del caso de uso
        - Spring llama a estos métodos automáticamente
        - Spring inyecta los parámetros (los puertos de salida)

        ### **Parámetros de los métodos**
        - `EventRepositoryPort` → Spring busca un bean que implemente esta interfaz → Encuentra `EventJpaAdapter`
        - `VenueRepositoryPort` → Spring busca un bean que implemente esta interfaz → Encuentra `VenueJpaAdapter`

        ### **Retornos**
        - Retornan las implementaciones concretas (`CreateEventUseCaseImpl`, etc.)
        - Pero el tipo de retorno es la **interfaz** (`CreateEventUseCase`)
        - Esto permite inyectar por interfaz en los controladores


        ## 🔄 **Flujo completo de inyección:**
        ```
        1. Spring inicia
        2. Detecta @Component en EventJpaAdapter
        3. Detecta @Configuration en ApplicationConfig
        4. Ejecuta método createEventUseCase()
        5. Inyecta EventJpaAdapter como EventRepositoryPort
        6. Crea instancia de CreateEventUseCaseImpl
        7. Registra el bean como CreateEventUseCase
        8. EventController pide CreateEventUseCase
        9. Spring inyecta el bean creado ✅

**/