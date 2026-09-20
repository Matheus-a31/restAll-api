package com.br.RestAll.comanda.controller;

import com.br.RestAll.comanda.dto.ComandaRequestDTO;
import com.br.RestAll.comanda.dto.ComandaResponseDTO;
import com.br.RestAll.comanda.entity.StatusComanda;
import com.br.RestAll.comanda.service.ComandaService;
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
class ComandaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ComandaService service;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveCriarComanda() throws Exception {
        ComandaRequestDTO request = new ComandaRequestDTO(101, 5, StatusComanda.ABERTA);
        ComandaResponseDTO response = new ComandaResponseDTO(1L, 101, 5, 1L, StatusComanda.ABERTA, LocalDateTime.now(), null, BigDecimal.ZERO);

        when(service.criar(any(ComandaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/comandas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numero").value(101))
                .andExpect(jsonPath("$.mesa").value(5));
    }

    @Test
    @WithMockUser(roles = "FUNCIONARIO")
    void deveListarComandas() throws Exception {
        ComandaResponseDTO response = new ComandaResponseDTO(1L, 101, 5, 1L, StatusComanda.ABERTA, LocalDateTime.now(), null, BigDecimal.ZERO);

        when(service.listar()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/comandas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numero").value(101));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveBuscarComandaPorId() throws Exception {
        ComandaResponseDTO response = new ComandaResponseDTO(1L, 101, 5, 1L, StatusComanda.ABERTA, LocalDateTime.now(), null, BigDecimal.ZERO);

        when(service.buscarPorId(1L)).thenReturn(response);

        mockMvc.perform(get("/api/comandas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(101));
    }

    @Test
    @WithMockUser(roles = "GERENTE")
    void deveAtualizarComanda() throws Exception {
        ComandaRequestDTO request = new ComandaRequestDTO(102, 6, StatusComanda.FECHADA);
        ComandaResponseDTO response = new ComandaResponseDTO(1L, 102, 6, 1L, StatusComanda.FECHADA, LocalDateTime.now(), LocalDateTime.now(), BigDecimal.ZERO);

        when(service.atualizar(eq(1L), any(ComandaRequestDTO.class))).thenReturn(response);

        mockMvc.perform(put("/api/comandas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numero").value(102))
                .andExpect(jsonPath("$.status").value("FECHADA"));
    }

    @Test
    @WithMockUser(roles = "ADMINISTRADOR")
    void deveRemoverComanda() throws Exception {
        doNothing().when(service).remover(1L);

        mockMvc.perform(delete("/api/comandas/1"))
                .andExpect(status().isNoContent());

        verify(service, times(1)).remover(1L);
    }
}
