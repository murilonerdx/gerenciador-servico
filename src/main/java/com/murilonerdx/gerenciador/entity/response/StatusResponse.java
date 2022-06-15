package com.murilonerdx.gerenciador.entity.response;

import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.Entity;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {
    private StatusVote status;
}
