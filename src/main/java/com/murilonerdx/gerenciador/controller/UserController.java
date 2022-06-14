package com.murilonerdx.gerenciador.controller;


import com.murilonerdx.gerenciador.controller.docs.UserControllerDocs;
import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<UserDTO> create(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok().body(service.criarUsuario(userRequestDTO));
    }

    @Override
    @GetMapping("/search-email/{email}")
    public ResponseEntity<UserDTO> findByEmail(@PathVariable("email") String email) throws EmailNotFoundException {
        return ResponseEntity.ok().body(service.procurarPorEmail(email));
    }

    @Override
    public void delete(Long id) {
        service.deletarUsuario(id);
    }

    @Override
    public ResponseEntity<UserDTO> update(Long id, UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok().body(service.atualizarUsuario(id, userRequestDTO));
    }
}
