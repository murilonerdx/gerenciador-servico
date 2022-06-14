package com.murilonerdx.gerenciador.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murilonerdx.gerenciador.entity.Rulling;
import com.murilonerdx.gerenciador.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    @JsonIgnore
    private Integer idRulling;
    private String email;
}
