package com.br.RestAll.cardapio.service;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

    @InjectMocks
    private ItemCardapioService service;

    @Test
    void deveCriarItemCardapioComSucesso() {
        ItemCardapioRequestDTO request = new ItemCardapioRequestDTO(
                "Hambúrguer", "Delicioso", "Lanches", new BigDecimal("25.00"), true, "img.jpg");

        ItemCardapio salvo = ItemCardapio.builder()
                .id(1L).nome("Hambúrguer").descricao("Delicioso").categoria("Lanches")
                .preco(new BigDecimal("25.00")).disponivel(true).imagem("img.jpg").build();

        when(repository.save(any(ItemCardapio.class))).thenReturn(salvo);

        ItemCardapioResponseDTO response = service.criar(request);

        assertNotNull(response);
        assertEquals("Hambúrguer", response.nome());
        verify(repository, times(1)).save(any(ItemCardapio.class));
    }

    @Test
    void deveListarItensCardapio() {
        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").build();
        when(repository.findAll()).thenReturn(List.of(item));

        List<ItemCardapioResponseDTO> itens = service.listar();

        assertFalse(itens.isEmpty());
        assertEquals(1, itens.size());
        assertEquals("Pizza", itens.get(0).nome());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").build();
        when(repository.findById(1L)).thenReturn(Optional.of(item));

        ItemCardapioResponseDTO response = service.buscarPorId(1L);

        assertNotNull(response);
        assertEquals("Pizza", response.nome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> service.buscarPorId(1L));
    }

    @Test
    void deveRemoverComSucesso() {
        ItemCardapio item = ItemCardapio.builder().id(1L).nome("Pizza").build();
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        doNothing().when(repository).delete(item);

        assertDoesNotThrow(() -> service.remover(1L));
        verify(repository, times(1)).delete(item);
    }
}
