package com.br.RestAll.usuario.dto;

import lombok.Data;

@Data
public class AtualizarUsuarioRequest {
    private String nome;
    private String email;
    private String cpf;
    private String cargo;
    private String telefone;
    private Boolean ativo;
}
