package com.br.RestAll.despesa.dto;

import com.br.RestAll.despesa.entity.CategoriaDespesa;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DespesaResponse {
    private Long id;
    private Long restauranteId;
    private String descricao;
    private BigDecimal valor;
    private LocalDate dataDespesa;
    private CategoriaDespesa categoria;
}
