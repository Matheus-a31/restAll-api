package com.br.RestAll.cardapio.controller;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.service.ItemCardapioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cardapio")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
public class ItemCardapioController {

    private final ItemCardapioService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemCardapioResponseDTO criar(@RequestBody @Valid ItemCardapioRequestDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<ItemCardapioResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ItemCardapioResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ItemCardapioResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ItemCardapioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
