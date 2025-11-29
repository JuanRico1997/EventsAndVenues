package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementación del caso de uso: Eliminar Evento.
 *
 * Contiene la lógica de negocio para eliminar un evento.
 */
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(DeleteEventUseCaseImpl.class);

    private final EventRepositoryPort eventRepository;

    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    @Override
    public void execute(Long id) {
        log.info("EVENT_DELETE_START eventId={}", id);

        // Verificar que existe antes de eliminar
        if (!eventRepository.existsById(id)) {
            log.error("EVENT_DELETE_FAILED eventId={} reason=EventNotFound", id);
            throw new EntityNotFoundException("Event not found with id: " + id);
        }

        eventRepository.deleteById(id);

        log.info("EVENT_DELETE_SUCCESS eventId={}", id);
    }
}