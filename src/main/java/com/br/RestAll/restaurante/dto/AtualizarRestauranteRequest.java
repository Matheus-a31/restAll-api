package com.br.RestAll.restaurante.dto;

import lombok.Data;
import com.br.RestAll.restaurante.entity.StatusRestaurante;

@Data
public class AtualizarRestauranteRequest {
    private String nome;
    private String telefone;
    private String email;
    private String endereco;
    private StatusRestaurante status;
}
