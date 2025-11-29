package com.riwi.EventAndVenue.infrastructure_hexagonal.adapters.in.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/**
 * Filtro para agregar traceId a MDC (Mapped Diagnostic Context).
 *
 * El traceId se propaga automáticamente a todos los logs generados
 * durante el procesamiento de una petición HTTP.
 *
 * MDC permite correlacionar todos los logs de una misma petición.
 */
@Component
@Order(1) // Se ejecuta primero
public class MDCFilter implements Filter {

    private static final String TRACE_ID = "traceId";
    private static final String REQUEST_ID = "requestId";
    private static final String USER_ID = "userId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        try {
            // Generar traceId único para esta petición
            String traceId = UUID.randomUUID().toString();
            MDC.put(TRACE_ID, traceId);

            // También agregar requestId (mismo valor por ahora)
            MDC.put(REQUEST_ID, traceId);

            // Agregar información adicional del request
            MDC.put("method", httpRequest.getMethod());
            MDC.put("path", httpRequest.getRequestURI());

            // TODO: Cuando implementemos JWT en TASK 3, extraer userId del token
            // Por ahora dejamos userId vacío
            MDC.put(USER_ID, "anonymous");

            // Continuar con la cadena de filtros
            chain.doFilter(request, response);

        } finally {
            // IMPORTANTE: Limpiar MDC al finalizar la petición
            // Si no se limpia, el traceId puede filtrarse a otras peticiones
            MDC.clear();
        }
    }
}