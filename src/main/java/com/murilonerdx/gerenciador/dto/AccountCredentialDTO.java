package com.murilonerdx.gerenciador.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;

@Data
@Getter
@Setter
public class AccountCredentialDTO {
    @Email(message="Digite um e-mail valido")
    private String email;
    private String password;
}
