package com.biblioteca.logros.repository;

import com.biblioteca.logros.model.LogroUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LogroUsuarioRepository extends JpaRepository<LogroUsuario, Long> {

    List<LogroUsuario> findByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);

    Optional<LogroUsuario> findByUsuarioIdAndLogroId(Long usuarioId, Long logroId);

    boolean existsByUsuarioIdAndLogroId(Long usuarioId, Long logroId);

    Integer countByUsuarioIdAndJuegoId(Long usuarioId, Long juegoId);
}
