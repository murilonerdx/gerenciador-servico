package com.murilonerdx.gerenciador.entity.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VoteSessionRequest {
    private String email;
}
