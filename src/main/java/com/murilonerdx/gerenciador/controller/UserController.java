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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/v1/api/users")
public class UserController implements UserControllerDocs {

    @Autowired
    private UserService service;

    @Override
    @GetMapping("/listar-usuarios")
    public ResponseEntity<List<UserDTO>> findAll() {
        return ResponseEntity.ok().body(service.listarTodosUsuarios());
    }

    @Override
    @PostMapping("/criar-usuario")
    public ResponseEntity<UserDTO> create(@RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarUsuario(userRequestDTO));
    }

    @Override
    @GetMapping("/buscar-email/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email) throws EmailNotFoundException {
        return ResponseEntity.ok().body(service.procurarPorEmail(email));
    }

    @Override
    @DeleteMapping("/excluir-usuario/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PutMapping(value = "/atualizar-usuario/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable("id") Long id, @RequestBody @Valid UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok().body(service.atualizarUsuario(id, userRequestDTO));
    }

    @Override
    @GetMapping(value = "/buscar-id/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable("id") Long id, UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping(value = "/{cpf}")
    public ResponseEntity<?> findByCPF(@PathVariable("cpf") @CPF(message = "Digite um CPF valido") String cpf) throws CpfNotFoundException {
        if (CPFValidation.isValid(cpf)) {
            return ResponseEntity.ok().body(service.buscarCPF(cpf));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
