package com.biblioteca.logros.repository;

import com.biblioteca.logros.model.Logro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogroRepository extends JpaRepository<Logro, Long> {

    List<Logro> findByJuegoId(Long juegoId);

    Integer countByJuegoId(Long juegoId);
}
