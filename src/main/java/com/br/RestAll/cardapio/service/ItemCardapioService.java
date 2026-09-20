package com.br.RestAll.cardapio.service;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
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
public class ItemCardapioService {

    private final ItemCardapioRepository repository;
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
    public ItemCardapioResponseDTO criar(ItemCardapioRequestDTO dto) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();

        ItemCardapio item = ItemCardapio.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .categoria(dto.categoria())
                .preco(dto.preco())
                .disponivel(dto.disponivel() != null ? dto.disponivel() : true)
                .imagem(dto.imagem())
                .restaurante(restaurante)
                .build();

        ItemCardapio salvo = repository.save(item);
        return ItemCardapioResponseDTO.fromEntity(salvo);
    }

    public List<ItemCardapioResponseDTO> listar() {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        return repository.findByRestauranteId(restaurante.getId()).stream()
                .map(ItemCardapioResponseDTO::fromEntity)
                .toList();
    }

    public ItemCardapioResponseDTO buscarPorId(Long id) {
        ItemCardapio item = getByIdAndRestaurante(id);
        return ItemCardapioResponseDTO.fromEntity(item);
    }

    @Transactional
    public ItemCardapioResponseDTO atualizar(Long id, ItemCardapioRequestDTO dto) {
        ItemCardapio item = getByIdAndRestaurante(id);
        
        item.setNome(dto.nome());
        item.setDescricao(dto.descricao());
        item.setCategoria(dto.categoria());
        item.setPreco(dto.preco());
        if (dto.disponivel() != null) {
            item.setDisponivel(dto.disponivel());
        }
        item.setImagem(dto.imagem());

        ItemCardapio atualizado = repository.save(item);
        return ItemCardapioResponseDTO.fromEntity(atualizado);
    }

    @Transactional
    public void remover(Long id) {
        ItemCardapio item = getByIdAndRestaurante(id);
        repository.delete(item);
    }

    private ItemCardapio getByIdAndRestaurante(Long id) {
        Restaurante restaurante = getRestauranteDoUsuarioLogado();
        return repository.findByIdAndRestauranteId(id, restaurante.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item do cardápio não encontrado ou não pertence ao seu restaurante"));
    }
}
