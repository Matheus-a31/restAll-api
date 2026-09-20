package com.br.RestAll.cardapio.service;

import com.br.RestAll.cardapio.dto.ItemCardapioRequestDTO;
import com.br.RestAll.cardapio.dto.ItemCardapioResponseDTO;
import com.br.RestAll.cardapio.entity.ItemCardapio;
import com.br.RestAll.cardapio.repository.ItemCardapioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemCardapioService {

    private final ItemCardapioRepository repository;

    @Transactional
    public ItemCardapioResponseDTO criar(ItemCardapioRequestDTO dto) {
        ItemCardapio item = ItemCardapio.builder()
                .nome(dto.nome())
                .descricao(dto.descricao())
                .categoria(dto.categoria())
                .preco(dto.preco())
                .disponivel(dto.disponivel() != null ? dto.disponivel() : true)
                .imagem(dto.imagem())
                .build();

        ItemCardapio salvo = repository.save(item);
        return ItemCardapioResponseDTO.fromEntity(salvo);
    }

    public List<ItemCardapioResponseDTO> listar() {
        return repository.findAll().stream()
                .map(ItemCardapioResponseDTO::fromEntity)
                .toList();
    }

    public ItemCardapioResponseDTO buscarPorId(Long id) {
        ItemCardapio item = getById(id);
        return ItemCardapioResponseDTO.fromEntity(item);
    }

    @Transactional
    public ItemCardapioResponseDTO atualizar(Long id, ItemCardapioRequestDTO dto) {
        ItemCardapio item = getById(id);
        
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
        ItemCardapio item = getById(id);
        repository.delete(item);
    }

    private ItemCardapio getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item do cardápio não encontrado"));
    }
}
