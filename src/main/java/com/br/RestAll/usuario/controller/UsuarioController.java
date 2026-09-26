package com.br.RestAll.usuario.controller;

import com.br.RestAll.usuario.dto.CriarUsuarioRequest;
import com.br.RestAll.usuario.dto.UsuarioResponse;
import com.br.RestAll.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuários")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping("/funcionario")
    @PreAuthorize("hasAnyRole('DONO', 'GERENTE', 'ADMINISTRADOR')")
    @Operation(summary = "Registra um funcionário (Acesso: DONO, GERENTE, ADMINISTRADOR)")
    public ResponseEntity<UsuarioResponse> registrarFuncionario(@Valid @RequestBody CriarUsuarioRequest request) {
        return ResponseEntity.status(201).body(usuarioService.criarFuncionario(request));
    }

    @PostMapping("/gerente")
    @PreAuthorize("hasAnyRole('DONO', 'ADMINISTRADOR')")
    @Operation(summary = "Registra um gerente (Acesso: DONO, ADMINISTRADOR)")
    public ResponseEntity<UsuarioResponse> registrarGerente(@Valid @RequestBody CriarUsuarioRequest request) {
        return ResponseEntity.status(201).body(usuarioService.criarGerente(request));
    }

    @PostMapping("/dono")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Registra um dono (Acesso: ADMINISTRADOR)")
    public ResponseEntity<UsuarioResponse> registrarDono(@Valid @RequestBody CriarUsuarioRequest request) {
        return ResponseEntity.status(201).body(usuarioService.criarDono(request));
    }

    @GetMapping("/funcionario")
    @PreAuthorize("hasAnyRole('DONO', 'GERENTE', 'ADMINISTRADOR')")
    @Operation(summary = "Lista todos os funcionários do restaurante atual (Acesso: DONO, GERENTE, ADMINISTRADOR)")
    public ResponseEntity<java.util.List<UsuarioResponse>> listarFuncionarios() {
        return ResponseEntity.ok(usuarioService.listarFuncionarios());
    }

    @GetMapping("/gerente")
    @PreAuthorize("hasAnyRole('DONO', 'ADMINISTRADOR')")
    @Operation(summary = "Lista todos os gerentes do restaurante atual (Acesso: DONO, ADMINISTRADOR)")
    public ResponseEntity<java.util.List<UsuarioResponse>> listarGerentes() {
        return ResponseEntity.ok(usuarioService.listarGerentes());
    }

    @DeleteMapping("/funcionario/{id}")
    @PreAuthorize("hasAnyRole('DONO', 'GERENTE', 'ADMINISTRADOR')")
    @Operation(summary = "Remove um funcionário (Acesso: DONO, GERENTE, ADMINISTRADOR)")
    public ResponseEntity<Void> removerFuncionario(@PathVariable Long id) {
        usuarioService.removerFuncionario(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/gerente/{id}")
    @PreAuthorize("hasAnyRole('DONO', 'ADMINISTRADOR')")
    @Operation(summary = "Remove um gerente (Acesso: DONO, ADMINISTRADOR)")
    public ResponseEntity<Void> removerGerente(@PathVariable Long id) {
        usuarioService.removerGerente(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/dono/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    @Operation(summary = "Remove um dono (Acesso: ADMINISTRADOR)")
    public ResponseEntity<Void> removerDono(@PathVariable Long id) {
        usuarioService.removerDono(id);
        return ResponseEntity.noContent().build();
    }
}
