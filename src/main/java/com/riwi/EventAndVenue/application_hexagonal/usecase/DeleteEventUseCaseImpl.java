package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Implementación del caso de uso: Eliminar Evento.
 *
 * Contiene la lógica de negocio para eliminar un evento.
 */
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteEventUseCaseImpl.class);
    private final EventRepositoryPort eventRepository;
    private final Counter eventsDeletedCounter;

    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepository,
                                  @Qualifier("eventsDeletedCounter") Counter eventsDeletedCounter){
        this.eventRepository = eventRepository;
        this.eventsDeletedCounter = eventsDeletedCounter;

    }

    @Transactional
    @Override
    public void execute(Long eventId) {
        log.info("EVENT_DELETE_START eventId={}", eventId);

        // Verificar que el evento existe antes de eliminar
        if (!eventRepository.existsById(eventId)) {
            log.error("EVENT_DELETE_FAILED eventId={} reason=EventNotFound", eventId);
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }

        // Eliminar el evento
        eventRepository.deleteById(eventId);

        // Incrementar contador de eventos eliminados
        eventsDeletedCounter.increment();

        log.info("EVENT_DELETE_SUCCESS eventId={}", eventId);
    }
}