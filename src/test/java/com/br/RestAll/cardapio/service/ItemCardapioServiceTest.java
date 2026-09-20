package com.br.RestAll.cardapio.service;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
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
class ItemCardapioServiceTest {

    @Mock
    private ItemCardapioRepository repository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ItemCardapioService service;

    private Restaurante restaurante;
    private Usuario usuarioLogado;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(10L);

        usuarioLogado = new Usuario();
        usuarioLogado.setEmail("teste@restall.com.br");
        usuarioLogado.setRestaurante(restaurante);

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("teste@restall.com.br", null)
        );
    }

    @Test
    void deveCriarItemCardapioComSucesso() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        ItemCardapioRequestDTO request = new ItemCardapioRequestDTO(
                "Hambúrguer", "Delicioso", "Lanches", new BigDecimal("25.00"), true, "img.jpg");

        ItemCardapio salvo = ItemCardapio.builder()
                .id(1L).nome("Hambúrguer").descricao("Delicioso").categoria("Lanches")
                .preco(new BigDecimal("25.00")).disponivel(true).imagem("img.jpg").restaurante(restaurante).build();

        when(repository.save(any(ItemCardapio.class))).thenReturn(salvo);

        ItemCardapioResponseDTO response = service.criar(request);

        assertNotNull(response);
        assertEquals("Hambúrguer", response.nome());
        verify(repository, times(1)).save(any(ItemCardapio.class));
    }

    @Test
    void deveListarItensCardapioDoRestaurante() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").restaurante(restaurante).build();
        when(repository.findByRestauranteId(10L)).thenReturn(List.of(item));

        List<ItemCardapioResponseDTO> itens = service.listar();

        assertFalse(itens.isEmpty());
        assertEquals(1, itens.size());
        assertEquals("Pizza", itens.get(0).nome());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").restaurante(restaurante).build();
        when(repository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.of(item));

        ItemCardapioResponseDTO response = service.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Pizza", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarPorIdOuSerDeOutroRestaurante() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));
        when(repository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.buscarPorId(1L));
    }

    @Test
    void deveRemoverComSucesso() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));

        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").restaurante(restaurante).build();
        when(repository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.of(item));
        doNothing().when(repository).delete(item);

        assertDoesNotThrow(() -> service.remover(1L));
        verify(repository, times(1)).delete(item);
    }
}
