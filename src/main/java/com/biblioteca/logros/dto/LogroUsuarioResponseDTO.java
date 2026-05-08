package com.biblioteca.logros.dto;

import java.time.LocalDateTime;

public record LogroUsuarioResponseDTO(
        Long id,
        Long usuarioId,
        Long juegoId,
        Long logroId,
        LocalDateTime desbloqueadoEn,
        LogroResponseDTO logro
) {
}
