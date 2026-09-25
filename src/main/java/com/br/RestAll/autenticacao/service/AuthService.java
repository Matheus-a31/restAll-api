package com.br.RestAll.autenticacao.service;

import com.br.RestAll.autenticacao.dto.LoginResponse;
import com.br.RestAll.autenticacao.dto.RegistroRequest;
import com.br.RestAll.comum.security.TokenService;
import com.br.RestAll.comum.security.UsuarioDetails;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.restaurante.entity.StatusRestaurante;
import com.br.RestAll.restaurante.repository.RestauranteRepository;
import com.br.RestAll.usuario.entity.Perfil;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Transactional
    public LoginResponse registrar(RegistroRequest request) {
        // Valida se o email ou CNPJ já existem
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já está em uso.");
        }
        if (restauranteRepository.findByCnpj(request.getCnpj()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CNPJ já está em uso.");
        }

        // 1. Cria o Restaurante
        Restaurante restaurante = Restaurante.builder()
                .nome(request.getNomeRestaurante())
                .cnpj(request.getCnpj())
                .status(StatusRestaurante.ATIVO)
                .build();
        restaurante = restauranteRepository.save(restaurante);

        // 2. Cria o Usuário (Dono)
        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .perfil(Perfil.DONO)
                .restaurante(restaurante)
                .cpf(request.getCpf())
                .telefone(request.getTelefone())
                .cargo(null)
                .build();
        usuario = usuarioRepository.save(usuario);

        // 3. Gera o token para realizar o login automático
        UsuarioDetails usuarioDetails = new UsuarioDetails(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getSenha(),
                usuario.getPerfil(),
                usuario.getRestaurante() != null ? usuario.getRestaurante().getId() : null,
                usuario.getCargo()
        );
        String token = tokenService.gerarToken(usuarioDetails);

        return LoginResponse.builder()
                .token(token)
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .restauranteId(restaurante.getId())
                .build();
    }
}
