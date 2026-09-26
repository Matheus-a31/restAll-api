package com.br.RestAll.autenticacao.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistroRequest {
    
    @NotBlank(message = "O nome do restaurante é obrigatório")
    private String nomeRestaurante;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    private String cpf;
    private String telefone;
}
