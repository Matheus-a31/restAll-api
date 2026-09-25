package com.br.RestAll.estoque.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import com.br.RestAll.estoque.dto.EstoqueRequest;
import com.br.RestAll.estoque.dto.EstoqueResponse;
import com.br.RestAll.estoque.service.EstoqueService;

@RestController 
@RequestMapping("/api/estoque")
@RequiredArgsConstructor 
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'ESTOQUISTA')")
public class EstoqueController {
    
    private final EstoqueService estoqueService;

    @PostMapping
    public ResponseEntity<EstoqueResponse> criar(@Valid @RequestBody EstoqueRequest request) {
        return ResponseEntity.status(201).body(estoqueService.criar(request));
    }

    @GetMapping
    public ResponseEntity<List<EstoqueResponse>> listar() {
        return ResponseEntity.ok(estoqueService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstoqueResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(estoqueService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstoqueResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EstoqueRequest request) {
        return ResponseEntity.ok(estoqueService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        estoqueService.remover(id);
        return ResponseEntity.noContent().build();
    }
}
