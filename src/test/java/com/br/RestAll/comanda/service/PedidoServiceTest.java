package com.br.RestAll.comanda.service;

import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
import com.br.RestAll.comanda.dto.PedidoRequestDTO;
import com.br.RestAll.comanda.dto.PedidoResponseDTO;
import com.br.RestAll.comanda.entity.Comanda;
import com.br.RestAll.comanda.entity.Pedido;
import com.br.RestAll.comanda.entity.StatusComanda;
import com.br.RestAll.comanda.entity.StatusPedido;
import com.br.RestAll.comanda.repository.ComandaRepository;
import com.br.RestAll.comanda.repository.PedidoRepository;
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
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ComandaRepository comandaRepository;

    @Mock
    private ItemCardapioRepository itemCardapioRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private PedidoService service;

    private Restaurante restaurante;
    private Usuario usuarioLogado;
    private Comanda comanda;
    private ItemCardapio item;

    @BeforeEach
    void setUp() {
        restaurante = new Restaurante();
        restaurante.setId(10L);

        usuarioLogado = new Usuario();
        usuarioLogado.setId(1L);
        usuarioLogado.setEmail("teste@restall.com.br");
        usuarioLogado.setRestaurante(restaurante);

        comanda = Comanda.builder().id(1L).numero(101).mesa(5).restaurante(restaurante).status(StatusComanda.ABERTA).build();
        
        item = ItemCardapio.builder().id(2L).nome("Hambúrguer").preco(new BigDecimal("20.00")).restaurante(restaurante).build();

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("teste@restall.com.br", null)
        );
    }

    @Test
    void deveCriarPedidoComSucessoEAtualizarTotalDaComanda() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));
        when(comandaRepository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.of(comanda));
        when(itemCardapioRepository.findByIdAndRestauranteId(2L, 10L)).thenReturn(Optional.of(item));

        PedidoRequestDTO request = new PedidoRequestDTO(1L, 2L, 2, "Sem cebola");

        Pedido salvo = Pedido.builder()
                .id(1L).comanda(comanda).item(item).quantidade(2).observacao("Sem cebola")
                .status(StatusPedido.PREPARANDO).precoUnitario(new BigDecimal("20.00"))
                .valorTotal(new BigDecimal("40.00")).build();

        when(pedidoRepository.save(any(Pedido.class))).thenReturn(salvo);
        
        // Mock the findByComandaId so atualizarValorTotalComanda works
        when(pedidoRepository.findByComandaId(1L)).thenReturn(List.of(salvo));

        PedidoResponseDTO response = service.criar(request);

        assertNotNull(response);
        assertEquals(2, response.quantidade());
        assertEquals("Sem cebola", response.observacao());
        assertEquals(new BigDecimal("40.00"), response.valorTotal());
        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(comandaRepository, times(1)).save(comanda);
        assertEquals(new BigDecimal("40.00"), comanda.getValorTotal());
    }

    @Test
    void naoDeveCriarPedidoSeComandaNaoEstiverAberta() {
        when(usuarioRepository.findByEmail("teste@restall.com.br")).thenReturn(Optional.of(usuarioLogado));
        comanda.setStatus(StatusComanda.FECHADA);
        when(comandaRepository.findByIdAndRestauranteId(1L, 10L)).thenReturn(Optional.of(comanda));

        PedidoRequestDTO request = new PedidoRequestDTO(1L, 2L, 2, "Sem cebola");

        assertThrows(ResponseStatusException.class, () -> service.criar(request));
        verify(pedidoRepository, never()).save(any(Pedido.class));
    }
}
