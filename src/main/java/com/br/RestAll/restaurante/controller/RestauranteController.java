package com.br.RestAll.restaurante.controller;

import com.br.RestAll.restaurante.dto.AtualizarRestauranteRequest;
import com.br.RestAll.restaurante.dto.RestauranteResponse;
import com.br.RestAll.restaurante.service.RestauranteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes")
public class RestauranteController {

    private final RestauranteService restauranteService;

    @PutMapping("/meu-restaurante")
    @PreAuthorize("hasAnyRole('DONO', 'ADMINISTRADOR')")
    @Operation(summary = "Edita as informações do restaurante atual")
    public ResponseEntity<RestauranteResponse> atualizarRestaurante(@RequestBody AtualizarRestauranteRequest request) {
        return ResponseEntity.ok(restauranteService.atualizarRestaurante(request));
    }
}
