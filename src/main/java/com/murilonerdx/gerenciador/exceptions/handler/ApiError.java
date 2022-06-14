package com.murilonerdx.gerenciador.exceptions.handler;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import lombok.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ApiError {
    private List<String> errors;

    public ApiError(BindingResult bindingResult){
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error->this.errors.add(error.getDefaultMessage()));
    }

    public ApiError(EmailNotFoundException e) {
        this.errors = (Collections.singletonList(e.getMessage()));
    }

    public ApiError(IllegalArgumentException e) {
        this.errors = (Collections.singletonList(e.getMessage()));
    }

    public ApiError(ResponseStatusException e) {
        this.errors = Collections.singletonList(e.getReason());
    }

    public ApiError(UserNotFoundException e){
        this.errors = Collections.singletonList(e.getMessage());
    }

    public ApiError(String e){
        this.errors = Collections.singletonList(e);
    }

    public List<String> getErrors() {
        return errors;
    }
}