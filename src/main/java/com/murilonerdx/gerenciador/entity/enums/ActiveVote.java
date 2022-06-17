package com.murilonerdx.gerenciador.entity.enums;

import lombok.*;

@Getter
@NoArgsConstructor
public enum ActiveVote {
    EV("ESPERANDO VOTAÇÃO"),
    VF("VOTAÇÃO FINALIZADA"),
    AIS("AGUARDANDO INICIO DA SESSÃO"),
    AAP("AGUARDANDO ABERTURA DA PAUTA");

    private String description;

    ActiveVote(String description) {
        this.description = description;
    }
}
