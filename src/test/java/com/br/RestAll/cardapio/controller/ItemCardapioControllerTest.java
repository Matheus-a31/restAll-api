package com.br.RestAll.cardapio.controller;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.service.ItemCardapioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ItemCardapioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemCardapioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void deveCriarItemCardapioComSucesso() throws Exception {
        ItemCardapioRequestDTO request = new ItemCardapioRequestDTO("Hambúrguer", "Desc", "Lanches", new BigDecimal("25.00"), true, null);
        ItemCardapioResponseDTO response = new ItemCardapioResponseDTO(1L, "Hambúrguer", "Desc", "Lanches", new BigDecimal("25.00"), true, null);

        when(service.criar(any(ItemCardapioRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/cardapio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Hambúrguer"));
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO")
    void deveListarItensCardapio() throws Exception {
        ItemCardapioResponseDTO response = new ItemCardapioResponseDTO(1L, "Hambúrguer", "Desc", "Lanches", new BigDecimal("25.00"), true, null);

        when(service.listar()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/cardapio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Hambúrguer"));
    }

    @Test
    @WithMockUser(roles = "DONO")
    void deveBuscarPorId() throws Exception {
        ItemCardapioResponseDTO response = new ItemCardapioResponseDTO(1L, "Hambúrguer", "Desc", "Lanches", new BigDecimal("25.00"), true, null);

        when(service.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/cardapio/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Hambúrguer"));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveRemoverItem() throws Exception {
        doNothing().when(service).remover(1L);

        mockMvc.perform(delete("/api/cardapio/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveNegarAcessoQuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/api/cardapio"))
                .andExpect(status().isForbidden());
    }
}
