package com.murilonerdx.gerenciador.exceptions;

import com.murilonerdx.gerenciador.entity.enums.ActiveVote;

public class VotacaoFinalizadaException extends Exception {
    public VotacaoFinalizadaException(ActiveVote active) {
        super("Essa pauta se encontra em estado de [" + active.getDescription() + "]" + " - " + active);
    }
}
