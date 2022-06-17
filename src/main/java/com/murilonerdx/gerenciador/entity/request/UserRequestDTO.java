package com.murilonerdx.gerenciador.entity.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    @NotEmpty(message="Por favor digite um nome!")
    private String name;

    @Email(message="Por favor digite um e-mail valido!")
    private String email;

    @CPF(message="Por favor digite um cpf valido")
    private String cpf;
}
