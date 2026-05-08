package com.biblioteca.logros.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DesbloquearLogroRequestDTO {

    @NotNull(message = "El usuarioId es obligatorio")
    private Long usuarioId;

    @NotNull(message = "El juegoId es obligatorio")
    private Long juegoId;

    @NotNull(message = "El logroId es obligatorio")
    private Long logroId;
}
