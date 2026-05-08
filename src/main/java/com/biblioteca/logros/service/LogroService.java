package com.biblioteca.logros.service;

import com.biblioteca.logros.client.JuegoClient;
import com.biblioteca.logros.client.UsuarioClient;
import com.biblioteca.logros.dto.DesbloquearLogroRequestDTO;
import com.biblioteca.logros.dto.JuegoDTO;
import com.biblioteca.logros.dto.LogroRequestDTO;
import com.biblioteca.logros.dto.LogroResponseDTO;
import com.biblioteca.logros.dto.LogroUsuarioResponseDTO;
import com.biblioteca.logros.dto.ProgresoLogrosDTO;
import com.biblioteca.logros.model.Logro;
import com.biblioteca.logros.model.LogroUsuario;
import com.biblioteca.logros.repository.LogroRepository;
import com.biblioteca.logros.repository.LogroUsuarioRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LogroService {

    private final LogroRepository logroRepository;
    private final LogroUsuarioRepository logroUsuarioRepository;
    private final JuegoClient juegoClient;
    private final UsuarioClient usuarioClient;

    public List<LogroResponseDTO> obtenerTodos() {
        return logroRepository.findAll().stream()
                .map(logro -> mapLogro(logro, false))
                .toList();
    }

    public Optional<LogroResponseDTO> obtenerPorId(Long id) {
        return logroRepository.findById(id).map(logro -> mapLogro(logro, false));
    }

    public List<LogroResponseDTO> obtenerPorJuego(Long juegoId) {
        validarJuego(juegoId);
        return logroRepository.findByJuegoId(juegoId).stream()
                .map(logro -> mapLogro(logro, false))
                .toList();
    }

    @Transactional
    public LogroResponseDTO crear(LogroRequestDTO dto) {
        validarJuego(dto.getJuegoId());
        Logro logro = new Logro(null, dto.getJuegoId(), dto.getNombre(), dto.getDescripcion());
        return mapLogro(logroRepository.save(logro), false);
    }

    @Transactional
    public Optional<LogroResponseDTO> actualizar(Long id, LogroRequestDTO dto) {
        validarJuego(dto.getJuegoId());
        return logroRepository.findById(id).map(logro -> {
            logro.setJuegoId(dto.getJuegoId());
            logro.setNombre(dto.getNombre());
            logro.setDescripcion(dto.getDescripcion());
            return mapLogro(logroRepository.save(logro), false);
        });
    }

    @Transactional
    public void eliminar(Long id) {
        if (!logroRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe un logro con id " + id);
        }
        logroRepository.deleteById(id);
    }

    @Transactional
    public LogroUsuarioResponseDTO desbloquear(DesbloquearLogroRequestDTO dto) {
        validarUsuario(dto.getUsuarioId());
        validarJuego(dto.getJuegoId());

        Logro logro = logroRepository.findById(dto.getLogroId())
                .orElseThrow(() -> new IllegalArgumentException("No existe un logro con id " + dto.getLogroId()));

        if (!logro.getJuegoId().equals(dto.getJuegoId())) {
            throw new IllegalArgumentException("El logro no pertenece al juego indicado");
        }

        LogroUsuario registro = logroUsuarioRepository
                .findByUsuarioIdAndLogroId(dto.getUsuarioId(), dto.getLogroId())
                .orElseGet(() -> new LogroUsuario(
                        null,
                        dto.getUsuarioId(),
                        dto.getJuegoId(),
                        dto.getLogroId(),
                        LocalDateTime.now()
                ));

        return mapLogroUsuario(logroUsuarioRepository.save(registro), logro);
    }

    public ProgresoLogrosDTO obtenerProgreso(Long usuarioId, Long juegoId) {
        validarUsuario(usuarioId);
        validarJuego(juegoId);

        List<Logro> logros = logroRepository.findByJuegoId(juegoId);
        Set<Long> logrosObtenidos = logroUsuarioRepository.findByUsuarioIdAndJuegoId(usuarioId, juegoId)
                .stream()
                .map(LogroUsuario::getLogroId)
                .collect(Collectors.toSet());

        int total = logros.size();
        int obtenidos = logrosObtenidos.size();
        double porcentaje = total == 0 ? 0.0 : (obtenidos * 100.0) / total;

        List<LogroResponseDTO> detalle = logros.stream()
                .map(logro -> mapLogro(logro, logrosObtenidos.contains(logro.getId())))
                .toList();

        return new ProgresoLogrosDTO(
                usuarioId,
                juegoId,
                total,
                obtenidos,
                porcentaje,
                obtenidos > 0,
                obtenerJuegoSeguro(juegoId),
                detalle
        );
    }

    private LogroResponseDTO mapLogro(Logro logro, Boolean desbloqueado) {
        return new LogroResponseDTO(
                logro.getId(),
                logro.getJuegoId(),
                logro.getNombre(),
                logro.getDescripcion(),
                desbloqueado
        );
    }

    private LogroUsuarioResponseDTO mapLogroUsuario(LogroUsuario registro, Logro logro) {
        return new LogroUsuarioResponseDTO(
                registro.getId(),
                registro.getUsuarioId(),
                registro.getJuegoId(),
                registro.getLogroId(),
                registro.getDesbloqueadoEn(),
                mapLogro(logro, true)
        );
    }

    private void validarUsuario(Long usuarioId) {
        try {
            usuarioClient.obtenerUsuario(usuarioId);
        } catch (WebClientResponseException.NotFound e) {
            throw new IllegalArgumentException("No existe un usuario con id " + usuarioId);
        }
    }

    private void validarJuego(Long juegoId) {
        try {
            juegoClient.obtenerJuego(juegoId);
        } catch (FeignException.NotFound e) {
            throw new IllegalArgumentException("No existe un juego con id " + juegoId);
        }
    }

    private JuegoDTO obtenerJuegoSeguro(Long juegoId) {
        try {
            return juegoClient.obtenerJuego(juegoId);
        } catch (FeignException e) {
            return null;
        }
    }
}
