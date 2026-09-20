package com.br.RestAll.comanda.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoRequestDTO(
        @NotNull(message = "O ID da comanda é obrigatório")
        Long comandaId,

        @NotNull(message = "O ID do item do cardápio é obrigatório")
        Long itemId,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser positiva")
        Integer quantidade,

        String observacao
) {}
