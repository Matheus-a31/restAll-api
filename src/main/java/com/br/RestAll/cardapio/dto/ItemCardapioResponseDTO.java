package com.br.RestAll.cardapio.dto;

import com.br.RestAll.cardapio.entity.ItemCardapio;

import java.math.BigDecimal;

public record ItemCardapioResponseDTO(
        Long id,
        String nome,
        String descricao,
        String categoria,
        BigDecimal preco,
        Boolean disponivel,
        String imagem
) {
    public static ItemCardapioResponseDTO fromEntity(ItemCardapio item) {
        return new ItemCardapioResponseDTO(
                item.getId(),
                item.getNome(),
                item.getDescricao(),
                item.getCategoria(),
                item.getPreco(),
                item.getDisponivel(),
                item.getImagem()
        );
    }
}
