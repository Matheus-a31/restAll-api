package com.br.RestAll.comanda.controller;

import com.br.RestAll.comanda.dto.PedidoRequestDTO;
import com.br.RestAll.comanda.dto.PedidoResponseDTO;
import com.br.RestAll.comanda.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMINISTRADOR', 'DONO', 'GERENTE', 'FUNCIONARIO')")
public class PedidoController {

    private final PedidoService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoResponseDTO criar(@RequestBody @Valid PedidoRequestDTO dto) {
        return service.criar(dto);
    }

    @GetMapping("/comanda/{comandaId}")
    public List<PedidoResponseDTO> listarPorComanda(@PathVariable Long comandaId) {
        return service.listarPorComanda(comandaId);
    }

    @GetMapping("/{id}")
    public PedidoResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        service.remover(id);
    }
}
