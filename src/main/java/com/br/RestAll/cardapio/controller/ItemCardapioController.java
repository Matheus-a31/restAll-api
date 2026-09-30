package com.br.RestAll.cardapio.controller;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.service.ItemCardapioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springdoc.core.annotations.ParameterObject;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cardapio")
@RequiredArgsConstructor
public class ItemCardapioController {

    private final ItemCardapioService service;

    @PostMapping(consumes = {"multipart/form-data"})
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
    public ItemCardapioResponseDTO criar(@ParameterObject @ModelAttribute @Valid ItemCardapioRequestDTO dto,
                                         @RequestPart(value = "imagem", required = false) MultipartFile file) {
        return service.criar(dto, file);
    }

    @GetMapping
    public List<ItemCardapioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/restaurante/{restauranteId}")
    @Operation(summary = "Lista os itens do cardápio de um restaurante específico (Acesso Público)")
    public List<ItemCardapioResponseDTO> listarPorRestaurante(@PathVariable Long restauranteId) {
        return service.listarPorRestaurante(restauranteId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
    public ItemCardapioResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
    public ItemCardapioResponseDTO atualizar(@PathVariable Long id, 
                                             @ParameterObject @ModelAttribute @Valid ItemCardapioRequestDTO dto,
                                             @RequestPart(value = "imagem", required = false) MultipartFile file) {
        return service.atualizar(id, dto, file);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
