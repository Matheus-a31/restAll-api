package com.br.RestAll.despesa.controller;

import com.br.RestAll.despesa.dto.DespesaRequest;
import com.br.RestAll.despesa.dto.DespesaResponse;
import com.br.RestAll.despesa.service.DespesaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE')")
public class DespesaController {

    private final DespesaService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DespesaResponse criar(@RequestBody @Valid DespesaRequest dto) {
        return service.criar(dto);
    }

    @GetMapping
    public List<DespesaResponse> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public DespesaResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public DespesaResponse atualizar(@PathVariable Long id, @RequestBody @Valid DespesaRequest dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
