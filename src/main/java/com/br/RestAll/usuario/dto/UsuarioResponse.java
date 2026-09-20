package com.br.RestAll.usuario.dto;

import com.br.RestAll.usuario.entity.Perfil;
import com.br.RestAll.usuario.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UsuarioResponse {
    private Long id;
    private String nome;
    private String email;
    private Perfil perfil;
    private Long restauranteId;
    private String cpf;
    private String cargo;
    private String telefone;
    private Boolean ativo;

    public static UsuarioResponse fromEntity(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .perfil(usuario.getPerfil())
                .restauranteId(usuario.getRestaurante() != null ? usuario.getRestaurante().getId() : null)
                .cpf(usuario.getCpf())
                .cargo(usuario.getCargo())
                .telefone(usuario.getTelefone())
                .ativo(usuario.getAtivo())
                .build();
    }
}
