package com.biblioteca.logros.controller;

import com.biblioteca.logros.dto.DesbloquearLogroRequestDTO;
import com.biblioteca.logros.dto.LogroRequestDTO;
import com.biblioteca.logros.dto.LogroResponseDTO;
import com.biblioteca.logros.dto.LogroUsuarioResponseDTO;
import com.biblioteca.logros.dto.ProgresoLogrosDTO;
import com.biblioteca.logros.service.LogroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v2/logros")
@Tag(name = "Logros", description = "Operaciones de logros y progreso")
@RequiredArgsConstructor
public class LogroController {

    private final LogroService logroService;

    @GetMapping
    @Operation(summary = "Listar todos los logros")
    public List<LogroResponseDTO> obtenerTodos() {
        return logroService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un logro por ID")
    public ResponseEntity<LogroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return logroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/juego/{juegoId}")
    @Operation(summary = "Listar logros de un juego")
    public List<LogroResponseDTO> obtenerPorJuego(@PathVariable Long juegoId) {
        return logroService.obtenerPorJuego(juegoId);
    }

    @GetMapping("/progreso/usuario/{usuarioId}/juego/{juegoId}")
    @Operation(summary = "Obtener progreso de logros por usuario y juego")
    public ProgresoLogrosDTO obtenerProgreso(
            @PathVariable Long usuarioId,
            @PathVariable Long juegoId) {
        return logroService.obtenerProgreso(usuarioId, juegoId);
    }

    @PostMapping
    @Operation(summary = "Crear un logro")
    public ResponseEntity<LogroResponseDTO> crear(@Valid @RequestBody LogroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logroService.crear(dto));
    }

    @PostMapping("/desbloquear")
    @Operation(summary = "Desbloquear un logro para un usuario")
    public ResponseEntity<LogroUsuarioResponseDTO> desbloquear(
            @Valid @RequestBody DesbloquearLogroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logroService.desbloquear(dto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un logro")
    public ResponseEntity<LogroResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LogroRequestDTO dto) {
        return logroService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un logro")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
