package com.murilonerdx.gerenciador.entity.response;

import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StatusResponse {
    private StatusVote status;
}
