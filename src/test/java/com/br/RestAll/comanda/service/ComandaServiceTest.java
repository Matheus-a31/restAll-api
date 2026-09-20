package com.br.RestAll.comanda.service;

import com.br.RestAll.comanda.dto.ComandaRequestDTO;
import com.br.RestAll.comanda.dto.ComandaResponseDTO;
import com.br.RestAll.comanda.entity.Comanda;
import com.br.RestAll.comanda.entity.StatusComanda;
import com.br.RestAll.comanda.repository.ComandaRepository;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComandaServiceTest {

    @Mock
    private ComandaRepository comandaRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ComandaService service;

    private Restaurante restaurante;
    private Usuario usuarioLogado;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(10L);

        usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);
        usuarioLogado.setEmail("teste@restall.com.br");
        usuarioLogado.setRestaurante(restaurante);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("teste@restall.com.br", null)
        );
    }

    @Test
    void deveCriarComandaComSucesso() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));
        when(comandaRepository.existsByMesaAndRestauranteIdAndStatus(5, 10L, StatusComanda.ABERTA)).thenReturn(false);

        ComandaRequestDTO request = new ComandaRequestDTO(101, 5, StatusComanda.ABERTA);

        Comanda salva = Comanda.builder()
                .id(1L).numero(101).mesa(5).funcionario(usuarioLogado).restaurante(restaurante)
                .status(StatusComanda.ABERTA).valorTotal(BigDecimal.ZERO).build();

        when(comandaRepository.save(any(Comanda.class))).thenReturn(salva);

        ComandaResponseDTO response = service.criar(request);

        assertNotNull(response);
        assertEquals(101, response.numero());
        assertEquals(5, response.mesa());
        verify(comandaRepository, times(1)).save(any(Comanda.class));
    }

    @Test
    void deveBloquearCriacaoDeComandaAbertaSeMesaJaTiverUma() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));
        when(comandaRepository.existsByMesaAndRestauranteIdAndStatus(5, 10L, StatusComanda.ABERTA)).thenReturn(true);

        ComandaRequestDTO request = new ComandaRequestDTO(101, 5, StatusComanda.ABERTA);

        assertThrows(ResponseStatusException.class, () -> service.criar(request));
        verify(comandaRepository, never()).save(any(Comanda.class));
    }

    @Test
    void deveListarComandasDoRestaurante() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        Comanda comanda = Comanda.builder().id(1L).numero(101).mesa(5).funcionario(usuarioLogado).restaurante(restaurante).build();
        when(comandaRepository.findByRestauranteId(10L)).thenReturn(List.of(comanda));

        List<ComandaResponseDTO> comandas = service.listar();

        assertFalse(comandas.isEmpty());
        assertEquals(1, comandas.size());
        assertEquals(101, comandas.get(0).numero());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        Comanda comanda = Comanda.builder().id(1L).numero(101).mesa(5).funcionario(usuarioLogado).restaurante(restaurante).build();
        when(comandaRepository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.of(comanda));

        ComandaResponseDTO response = service.buscarPorId(1L);

        assertNotNull(response);
        assertEquals(101, response.numero());
    }
}
