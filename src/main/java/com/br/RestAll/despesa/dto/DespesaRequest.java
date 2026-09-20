package com.br.RestAll.despesa.dto;

import com.br.RestAll.despesa.entity.CategoriaDespesa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class DespesaRequest {

    @NotBlank(message = "A descrição não pode estar em branco")
    private String descricao;

    @NotNull(message = "O valor não pode ser nulo")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal valor;

    @NotNull(message = "A data da despesa não pode ser nula")
    private LocalDate dataDespesa;

    @NotNull(message = "A categoria não pode ser nula")
    private CategoriaDespesa categoria;
}
