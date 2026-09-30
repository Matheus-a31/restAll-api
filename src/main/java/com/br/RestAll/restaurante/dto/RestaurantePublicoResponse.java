package com.br.RestAll.restaurante.dto;

import com.br.RestAll.restaurante.entity.Restaurante;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RestaurantePublicoResponse {
    private Long id;
    private String nome;

    public static RestaurantePublicoResponse fromEntity(Restaurante restaurante) {
        if (restaurante == null) return null;
        return RestaurantePublicoResponse.builder()
                .id(restaurante.getId())
                .nome(restaurante.getNome())
                .build();
    }
}
