package com.biblioteca.logros.controller;

import com.biblioteca.logros.dto.DesbloquearLogroRequestDTO;
import com.biblioteca.logros.dto.LogroRequestDTO;
import com.biblioteca.logros.dto.LogroResponseDTO;
import com.biblioteca.logros.dto.LogroUsuarioResponseDTO;
import com.biblioteca.logros.dto.ProgresoLogrosDTO;
import com.biblioteca.logros.service.LogroService;
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
@RequestMapping("/api/logros")
@RequiredArgsConstructor
public class LogroController {

    private final LogroService logroService;

    @GetMapping
    public List<LogroResponseDTO> obtenerTodos() {
        return logroService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LogroResponseDTO> obtenerPorId(@PathVariable Long id) {
        return logroService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/juego/{juegoId}")
    public List<LogroResponseDTO> obtenerPorJuego(@PathVariable Long juegoId) {
        return logroService.obtenerPorJuego(juegoId);
    }

    @GetMapping("/progreso/usuario/{usuarioId}/juego/{juegoId}")
    public ProgresoLogrosDTO obtenerProgreso(
            @PathVariable Long usuarioId,
            @PathVariable Long juegoId) {
        return logroService.obtenerProgreso(usuarioId, juegoId);
    }

    @PostMapping
    public ResponseEntity<LogroResponseDTO> crear(@Valid @RequestBody LogroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logroService.crear(dto));
    }

    @PostMapping("/desbloquear")
    public ResponseEntity<LogroUsuarioResponseDTO> desbloquear(
            @Valid @RequestBody DesbloquearLogroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(logroService.desbloquear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LogroResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LogroRequestDTO dto) {
        return logroService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        logroService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
