package com.riwi.EventAndVenue.application_hexagonal.usecase;

import com.riwi.EventAndVenue.domain_hexagonal.ports.in.DeleteEventUseCase;
import com.riwi.EventAndVenue.domain_hexagonal.ports.out.EventRepositoryPort;

/**
 * Implementación del caso de uso: Eliminar Evento.
 *
 * Contiene la lógica de negocio para eliminar un evento.
 */
public class DeleteEventUseCaseImpl implements DeleteEventUseCase {

    private final EventRepositoryPort eventRepository;

    public DeleteEventUseCaseImpl(EventRepositoryPort eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public void execute(Long id) {
        // REGLA 1: Verificar que el evento existe antes de eliminar
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Evento con ID " + id + " no encontrado");
        }

        // Eliminar el evento
        eventRepository.deleteById(id);
    }
}