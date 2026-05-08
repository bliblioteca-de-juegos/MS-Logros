package com.biblioteca.logros.dto;

import java.util.List;

public record ProgresoLogrosDTO(
        Long usuarioId,
        Long juegoId,
        Integer totalLogros,
        Integer logrosObtenidos,
        Double porcentajeCompletado,
        Boolean tieneLogros,
        JuegoDTO juego,
        List<LogroResponseDTO> logros
) {
}
