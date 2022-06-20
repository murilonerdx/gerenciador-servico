package com.murilonerdx.gerenciador.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
