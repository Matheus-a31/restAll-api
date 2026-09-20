package com.br.RestAll.comanda.controller;

import com.br.RestAll.comanda.dto.ComandaRequestDTO;
import com.br.RestAll.comanda.dto.ComandaResponseDTO;
import com.br.RestAll.comanda.service.ComandaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comandas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
public class ComandaController {

    private final ComandaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ComandaResponseDTO criar(@RequestBody @Valid ComandaRequestDTO dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<ComandaResponseDTO> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ComandaResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ComandaResponseDTO atualizar(@PathVariable Long id, @RequestBody @Valid ComandaRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
