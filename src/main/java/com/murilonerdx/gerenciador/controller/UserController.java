package com.murilonerdx.gerenciador.controller;


import com.murilonerdx.gerenciador.controller.docs.UserControllerDocs;
import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.exceptions.CpfNotFoundException;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.service.UserService;
import com.murilonerdx.gerenciador.util.CPFValidation;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/v1/api/user")
public class UserController implements UserControllerDocs {

    @Autowired
    private UserService service;

    @Override
    @GetMapping(value="/", produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<List<UserDTO>> findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok().body(service.listarTodosUsuarios(pageable));
    }

    @Override
    @PostMapping(value="/", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarUsuario(userRequestDTO));
    }

    @Override
    @GetMapping(value="/search-email/{email}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email) throws EmailNotFoundException {
        return ResponseEntity.ok().body(service.procurarPorEmail(email));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(value = "/{id}",
            produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok().body(service.atualizarUsuario(id, userRequestDTO));
    }

    @Override
    @GetMapping(value = "/search-id/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping(value = "/{cpf}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> findByCPF(@PathVariable("cpf") @CPF(message = "Digite um CPF valido") String cpf) throws CpfNotFoundException {
        if (CPFValidation.isValid(cpf)) {
            return ResponseEntity.ok().body(service.buscarCPF(cpf));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
