package com.murilonerdx.gerenciador.exceptions;

public class EmailNotFoundException extends Exception {
    public EmailNotFoundException(String s) {
        super("Digite um email cadastrado, e-mail: " + s + " não encontrado");
    }
}
