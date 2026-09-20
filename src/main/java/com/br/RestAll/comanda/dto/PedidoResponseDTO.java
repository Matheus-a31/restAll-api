package com.br.RestAll.comanda.dto;

import com.br.RestAll.comanda.entity.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoResponseDTO(
        Long id,
        Long comandaId,
        Long itemId,
        Integer quantidade,
        String observacao,
        StatusPedido status,
        BigDecimal precoUnitario,
        BigDecimal valorTotal,
        LocalDateTime dataPedido
) {}
