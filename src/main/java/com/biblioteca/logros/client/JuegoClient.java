package com.biblioteca.logros.client;

import com.biblioteca.logros.dto.JuegoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-juegos", url = "${ms.juegos.url}")
public interface JuegoClient {

    @GetMapping("/api/juegos/{id}")
    JuegoDTO obtenerJuego(@PathVariable Long id);
}
