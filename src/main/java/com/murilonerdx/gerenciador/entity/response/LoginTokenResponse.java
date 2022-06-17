package com.murilonerdx.gerenciador.entity.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginTokenResponse {
    private String email;
    private String token;
}
