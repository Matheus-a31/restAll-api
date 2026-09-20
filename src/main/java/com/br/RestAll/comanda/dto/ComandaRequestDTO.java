package com.br.RestAll.comanda.dto;

import com.br.RestAll.comanda.entity.StatusComanda;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ComandaRequestDTO(
        @NotNull(message = "O número da comanda é obrigatório")
        @Positive(message = "O número deve ser positivo")
        Integer numero,

        @NotNull(message = "A mesa é obrigatória")
        @Positive(message = "A mesa deve ser positiva")
        Integer mesa,

        @NotNull(message = "O status é obrigatório")
        StatusComanda status
) {}
