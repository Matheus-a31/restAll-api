package com.br.RestAll.restaurante.controller;

import com.br.RestAll.restaurante.dto.AtualizarRestauranteRequest;
import com.br.RestAll.restaurante.dto.RestaurantePublicoResponse;
import com.br.RestAll.restaurante.dto.RestauranteResponse;
import com.br.RestAll.restaurante.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @GetMapping
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Lista todos os restaurantes (Apenas Admin do Sistema)")
    public ResponseEntity<List<RestauranteResponse>> listarTodos() {
        return ResponseEntity.ok(restauranteService.listarTodos());
    }

    @GetMapping("/publico")
    @Operation(summary = "Lista todos os restaurantes ativos (ID e Nome) para acesso público")
    public ResponseEntity<List<RestaurantePublicoResponse>> listarPublicos() {
        return ResponseEntity.ok(restauranteService.listarPublicos());
    }

    @GetMapping("/meu-restaurante")
    @PreAuthorize("hasRole('DONO')")
    @Operation(summary = "Busca as informações do restaurante atual (Apenas Dono)")
    public ResponseEntity<RestauranteResponse> buscarMeuRestaurante() {
        return ResponseEntity.ok(restauranteService.buscarMeuRestaurante());
    }

    @PutMapping("/meu-restaurante")
    @PreAuthorize("hasRole('DONO')")
    @Operation(summary = "Edita as informações do restaurante atual (Apenas Dono)")
    public ResponseEntity<RestauranteResponse> atualizarMeuRestaurante(@RequestBody AtualizarRestauranteRequest request) {
        return ResponseEntity.ok(restauranteService.atualizarMeuRestaurante(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Edita as informações de qualquer restaurante (Apenas Admin do Sistema)")
    public ResponseEntity<RestauranteResponse> atualizarRestauranteComoAdmin(@PathVariable Long id, @RequestBody AtualizarRestauranteRequest request) {
        return ResponseEntity.ok(restauranteService.atualizarRestauranteComoAdmin(id, request));
    }
}
