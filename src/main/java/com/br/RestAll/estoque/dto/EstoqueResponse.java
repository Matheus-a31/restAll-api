package com.br.RestAll.estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor 
@AllArgsConstructor 
public class EstoqueResponse {
    private Long id;
    private Long restauranteId;
    private String nomeProduto;
    private LocalDate dataValidade;
    private Integer quantidade;
    private BigDecimal precoUnitario;
}
