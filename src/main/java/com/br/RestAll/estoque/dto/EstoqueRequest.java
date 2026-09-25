package com.br.RestAll.estoque.dto;


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
public class EstoqueRequest {
    

    @NotBlank (message = "O nome do produto não pode estar em branco")
    private String nomeProduto;

    private LocalDate dataValidade;

    @NotBlank(message = "A unidade de medida não pode estar em branco")
    private String unidadeMedida;

    @Positive(message = "A quantidade deve ser positiva")
    private Integer quantidade;

    @NotNull(message = "O valor não pode ser nulo")
    @Positive(message = "O valor deve ser positivo")
    private BigDecimal precoUnitario;
}
