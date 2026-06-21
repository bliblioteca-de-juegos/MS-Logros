package com.biblioteca.logros.service;

import com.biblioteca.logros.client.JuegoClient;
import com.biblioteca.logros.client.UsuarioClient;
import com.biblioteca.logros.dto.LogroResponseDTO;
import com.biblioteca.logros.model.Logro;
import com.biblioteca.logros.repository.LogroRepository;
import com.biblioteca.logros.repository.LogroUsuarioRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogroServiceTest {

    @Mock
    private LogroRepository logroRepository;
    @Mock
    private LogroUsuarioRepository logroUsuarioRepository;
    @Mock
    private JuegoClient juegoClient;
    @Mock
    private UsuarioClient usuarioClient;
    @InjectMocks
    private LogroService logroService;

    private final Faker faker = new Faker();

    @Test
    void obtenerTodosRetornaLosLogrosRegistrados() {
        String nombre = faker.lorem().word();
        String descripcion = faker.lorem().sentence();
        when(logroRepository.findAll()).thenReturn(List.of(new Logro(1L, 20L, nombre, descripcion)));

        List<LogroResponseDTO> resultado = logroService.obtenerTodos();

        assertEquals(1, resultado.size());
        assertEquals(nombre, resultado.getFirst().nombre());
        assertEquals(descripcion, resultado.getFirst().descripcion());
    }

    @Test
    void eliminarLanzaExcepcionCuandoElLogroNoExiste() {
        Long id = faker.number().numberBetween(1L, 1000L);
        when(logroRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> logroService.eliminar(id));
        verify(logroRepository, never()).deleteById(id);
    }
}
