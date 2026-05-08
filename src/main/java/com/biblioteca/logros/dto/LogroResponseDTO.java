package com.biblioteca.logros.dto;

public record LogroResponseDTO(
        Long id,
        Long juegoId,
        String nombre,
        String descripcion,
        Boolean desbloqueado
) {
}
