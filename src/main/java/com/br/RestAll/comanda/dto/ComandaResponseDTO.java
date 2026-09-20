package com.br.RestAll.comanda.dto;

import com.br.RestAll.comanda.entity.StatusComanda;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComandaResponseDTO(
        Long id,
        Integer numero,
        Integer mesa,
        Long funcionarioId,
        StatusComanda status,
        LocalDateTime dataAbertura,
        LocalDateTime dataFechamento,
        BigDecimal valorTotal
) {}
