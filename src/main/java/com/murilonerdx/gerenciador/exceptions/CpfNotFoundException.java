package com.murilonerdx.gerenciador.exceptions;

public class CpfNotFoundException extends Exception {
    public CpfNotFoundException(String s) {
        super("CPF: " + s + " não encontrado");
    }
}
