package com.br.RestAll.comanda.controller;

import com.br.RestAll.comanda.dto.PedidoRequestDTO;
import com.br.RestAll.comanda.dto.PedidoResponseDTO;
import com.br.RestAll.comanda.entity.StatusPedido;
import com.br.RestAll.comanda.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.br.RestAll.TestcontainersConfig;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestcontainersConfig.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PedidoService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveCriarPedido() throws Exception {
        PedidoRequestDTO request = new PedidoRequestDTO(1L, 2L, 2, "Sem cebola");
        PedidoResponseDTO response = new PedidoResponseDTO(1L, 1L, 2L, 2, "Sem cebola", StatusPedido.PREPARANDO, new BigDecimal("20.00"), new BigDecimal("40.00"), LocalDateTime.now());

        when(service.criar(any(PedidoRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/pedidos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidade").value(2))
                .andExpect(jsonPath("$.observacao").value("Sem cebola"));
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO")
    void deveListarPedidosPorComanda() throws Exception {
        PedidoResponseDTO response = new PedidoResponseDTO(1L, 1L, 2L, 2, "Sem cebola", StatusPedido.PREPARANDO, new BigDecimal("20.00"), new BigDecimal("40.00"), LocalDateTime.now());

        when(service.listarPorComanda(1L)).thenReturn(List.of(response));

        mockMvc.perform(get("/api/pedidos/comanda/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].quantidade").value(2));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveBuscarPedidoPorId() throws Exception {
        PedidoResponseDTO response = new PedidoResponseDTO(1L, 1L, 2L, 2, "Sem cebola", StatusPedido.PREPARANDO, new BigDecimal("20.00"), new BigDecimal("40.00"), LocalDateTime.now());

        when(service.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/pedidos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidade").value(2));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void deveRemoverPedido() throws Exception {
        doNothing().when(service).remover(1L);

        mockMvc.perform(delete("/api/pedidos/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).remover(1L);
    }
}
