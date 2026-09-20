package com.br.RestAll.autenticacao.dto;

import com.br.RestAll.usuario.entity.Perfil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private Long restauranteId;
}
