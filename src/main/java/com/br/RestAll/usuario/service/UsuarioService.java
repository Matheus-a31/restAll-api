package com.br.RestAll.usuario.service;

import com.br.RestAll.comum.context.ContextoRestaurante;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.restaurante.repository.RestauranteRepository;
import com.br.RestAll.usuario.dto.CriarUsuarioRequest;
import com.br.RestAll.usuario.dto.UsuarioResponse;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final ContextoRestaurante contextoRestaurante;

    @Transactional
    public UsuarioResponse criarFuncionario(CriarUsuarioRequest request) {
        return criarUsuario(request, Perfil.FUNCIONARIO);
    }

    @Transactional
    public UsuarioResponse criarGerente(CriarUsuarioRequest request) {
        return criarUsuario(request, Perfil.GERENTE);
    }

    @Transactional
    public UsuarioResponse criarDono(CriarUsuarioRequest request) {
        if (request.getRestauranteId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID do restaurante é obrigatório para criar um DONO.");
        }
        return criarUsuario(request, Perfil.DONO);
    }

    private UsuarioResponse criarUsuario(CriarUsuarioRequest request, Perfil perfil) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "E-mail já está em uso.");
        }

        Restaurante restaurante = null;

        if (perfil == Perfil.DONO) {
            restaurante = restauranteRepository.findById(request.getRestauranteId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado."));
        } else {
            Long myRestauranteId = contextoRestaurante.getRestauranteId();
            if (myRestauranteId == null) {
                // Se o ADMIN tentar criar um funcionario/gerente, ele precisa informar o restaurante? 
                // Por agora, assumimos que quem cria gerente/funcionario é alguém logado no seu restaurante.
                // Mas se o admin tentar criar sem estar atrelado a um, ele deve falhar, a não ser que passe o restauranteId.
                if (request.getRestauranteId() != null) {
                    restaurante = restauranteRepository.findById(request.getRestauranteId())
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado."));
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Administradores precisam informar o restauranteId para criar funcionários.");
                }
            } else {
                restaurante = restauranteRepository.findById(myRestauranteId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Seu restaurante não foi encontrado."));
            }
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .perfil(perfil)
                .restaurante(restaurante)
                .cpf(request.getCpf())
                .cargo(request.getCargo())
                .telefone(request.getTelefone())
                .build();

        usuario = usuarioRepository.save(usuario);
        return UsuarioResponse.fromEntity(usuario);
    }

    @Transactional(readOnly = true)
    public java.util.List<UsuarioResponse> listarFuncionarios() {
        Long restauranteId = contextoRestaurante.getRestauranteId();
        if (restauranteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não está associado a um restaurante.");
        }
        return usuarioRepository.findByRestauranteIdAndPerfil(restauranteId, Perfil.FUNCIONARIO)
                .stream()
                .map(UsuarioResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public java.util.List<UsuarioResponse> listarGerentes() {
        Long restauranteId = contextoRestaurante.getRestauranteId();
        if (restauranteId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não está associado a um restaurante.");
        }
        return usuarioRepository.findByRestauranteIdAndPerfil(restauranteId, Perfil.GERENTE)
                .stream()
                .map(UsuarioResponse::fromEntity)
                .toList();
    }
}
