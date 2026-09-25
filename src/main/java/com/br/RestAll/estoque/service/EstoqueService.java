package com.br.RestAll.estoque.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.br.RestAll.estoque.dto.EstoqueRequest;
import com.br.RestAll.estoque.dto.EstoqueResponse;
import com.br.RestAll.estoque.entity.Estoque;
import com.br.RestAll.estoque.repository.EstoqueRepository;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor 
public class EstoqueService {

    private final EstoqueRepository repository;
    private final UsuarioRepository usuarioRepository;

    private Restaurante getRestauranteDoUsuarioLogado() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não encontrado"));
        
        if (usuario.getRestaurante() == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Usuário não está associado a nenhum restaurante");
        }
        return usuario.getRestaurante();
    }

    @Transactional 

    public EstoqueResponse criar(EstoqueRequest dto) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();

        Estoque estoque = Estoque.builder()
                .nomeProduto(dto.getNomeProduto())
                .dataValidade(dto.getDataValidade())
                .quantidade(dto.getQuantidade())
                .precoUnitario(dto.getPrecoUnitario())
                .restaurante(restaurante)
                .build();

        Estoque salva = repository.save(estoque);
        return toResponse(salva);
    }

    private EstoqueResponse toResponse(Estoque estoque) {
        return EstoqueResponse.builder()
                .id(estoque.getId())
                .nomeProduto(estoque.getNomeProduto())
                .dataValidade(estoque.getDataValidade())
                .quantidade(estoque.getQuantidade())
                .precoUnitario(estoque.getPrecoUnitario())
                .build();
    }
}
