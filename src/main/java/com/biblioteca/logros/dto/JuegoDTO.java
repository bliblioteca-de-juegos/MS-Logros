package com.biblioteca.logros.dto;

public record JuegoDTO(
        Long id,
        String nombre,
        String titulo,
        String descripcion,
        Double precio
) {
}
