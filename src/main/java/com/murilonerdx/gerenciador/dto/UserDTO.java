package com.murilonerdx.gerenciador.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murilonerdx.gerenciador.entity.Rulling;
import com.murilonerdx.gerenciador.entity.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String name;
    @JsonIgnore
    private Integer idRulling;
    private String email;
}
