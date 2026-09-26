package com.br.RestAll.restaurante.dto;

import com.br.RestAll.restaurante.entity.Restaurante;
import com.br.RestAll.restaurante.entity.StatusRestaurante;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestauranteResponse {
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private String endereco;
    private StatusRestaurante status;

    public static RestauranteResponse fromEntity(Restaurante restaurante) {
        if (restaurante == null) return null;
        return RestauranteResponse.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .cnpj(restaurante.getCnpj())
                .telefone(restaurante.getTelefone())
                .email(restaurante.getEmail())
                .endereco(restaurante.getEndereco())
                .status(restaurante.getStatus())
                .build();
    }
}
