package com.br.RestAll.despesa.service;

import com.br.RestAll.despesa.dto.DespesaRequest;
import com.br.RestAll.despesa.dto.DespesaResponse;
import com.br.RestAll.despesa.entity.Despesa;
import com.br.RestAll.despesa.repository.DespesaRepository;
import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.usuario.entity.Usuario;
import com.br.RestAll.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository repository;
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
    public DespesaResponse criar(DespesaRequest dto) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();

        Despesa despesa = Despesa.builder()
                .descricao(dto.getDescricao())
                .valor(dto.getValor())
                .dataDespesa(dto.getDataDespesa())
                .categoria(dto.getCategoria())
                .restaurante(restaurante)
                .build();

        Despesa salva = repository.save(despesa);
        return toResponse(salva);
    }

    public List<DespesaResponse> listar() {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        return repository.findByRestauranteId(restaurante.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    public DespesaResponse buscarPorId(Long id) {
        Despesa despesa = getByIdAndRestaurante(id);
        return toResponse(despesa);
    }

    @Transactional
    public DespesaResponse atualizar(Long id, DespesaRequest dto) {
        Despesa despesa = getByIdAndRestaurante(id);
        
        despesa.setDescricao(dto.getDescricao());
        despesa.setValor(dto.getValor());
        despesa.setDataDespesa(dto.getDataDespesa());
        despesa.setCategoria(dto.getCategoria());

        Despesa atualizada = repository.save(despesa);
        return toResponse(atualizada);
    }

    @Transactional
    public void remover(Long id) {
        Despesa despesa = getByIdAndRestaurante(id);
        repository.delete(despesa);
    }

    private Despesa getByIdAndRestaurante(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        return repository.findById(id)
                .filter(d -> d.getRestaurante().getId().equals(restaurante.getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Despesa não encontrada ou não pertence ao seu restaurante"));
    }

    private DespesaResponse toResponse(Despesa despesa) {
        return DespesaResponse.builder()
                .id(despesa.getId())
                .restauranteId(despesa.getRestaurante().getId())
                .descricao(despesa.getDescricao())
                .valor(despesa.getValor())
                .dataDespesa(despesa.getDataDespesa())
                .categoria(despesa.getCategoria())
                .build();
    }
}
