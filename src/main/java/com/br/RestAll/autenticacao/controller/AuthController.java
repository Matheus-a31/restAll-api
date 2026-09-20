package com.br.RestAll.autenticacao.controller;

import com.br.RestAll.autenticacao.dto.LoginRequest;
import com.br.RestAll.autenticacao.dto.LoginResponse;
import com.br.RestAll.autenticacao.dto.RegistroRequest;
import com.br.RestAll.autenticacao.service.AuthService;
import com.br.RestAll.comum.context.ContextoRestaurante;
import com.br.RestAll.comum.security.TokenService;
import com.br.RestAll.comum.security.UsuarioDetails;
import com.br.RestAll.usuario.dto.UsuarioResponse;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/autenticacao")
@RequiredArgsConstructor
@Tag(name = "Autenticação")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final ContextoRestaurante contextoRestaurante;
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Autentica um usuário e retorna o token JWT")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UsuarioDetails usuarioDetails = (UsuarioDetails) authentication.getPrincipal();
        String token = tokenService.gerarToken(usuarioDetails);
        Usuario usuario = usuarioRepository.findById(usuarioDetails.getId()).orElseThrow();

        return ResponseEntity.ok(LoginResponse.builder()
                .token(token)
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .restauranteId(usuario.getRestaurante() != null ? usuario.getRestaurante().getId() : null)
                .build());
    }

    @PostMapping("/registro")
    @Operation(summary = "Registra um novo restaurante junto com o usuário dono, retornando o token JWT")
    public ResponseEntity<LoginResponse> registro(@Valid @RequestBody RegistroRequest request) {
        return ResponseEntity.status(201).body(authService.registrar(request));
    }

    @GetMapping("/me")
    @Operation(summary = "Retorna os dados do usuário logado baseado no token")
    public ResponseEntity<UsuarioResponse> me() {
        Usuario usuario = usuarioRepository.findById(contextoRestaurante.getUsuarioId()).orElseThrow();
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuario));
    }
}
