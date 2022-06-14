package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFound;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> listarTodosUsuarios() {
        return DozerConverter.parseListObjects(repository.findAll(), UserDTO.class);
    }

    public UserDTO criarUsuario(UserRequestDTO userDTO) {
        User user = DozerConverter.parseObject(userDTO, User.class);
        return DozerConverter.parseObject(repository.save(user), UserDTO.class);
    }

    public UserDTO procurarPorEmail(String email) throws EmailNotFoundException {
        try {
            return DozerConverter.parseObject(repository.findByEmail(email), UserDTO.class);
        } catch (Exception e) {
            throw new EmailNotFoundException(email);
        }
    }

    public void deletarUsuario(Long id) {
        try {
            User user = repository.findById(id)
                    .orElseThrow(()->
                            new UserNotFound("Usuario com id: " + id + " não encontrado"));
            repository.delete(user);
        } catch (UserNotFound e) {
            e.printStackTrace();
        }
    }


    public UserDTO atualizarUsuario(Long id, UserRequestDTO userDTO) {
        try {
            User newUser = repository.findById(id).orElseThrow(()->
                    new UserNotFound("Usuario com id: " + id + " não encontrado"));

            User oldUser = DozerConverter.parseObject(userDTO, User.class);
            oldUser.setId(newUser.getId());

            return DozerConverter.parseObject(repository.save(oldUser), UserDTO.class);
        } catch (UserNotFound e) {
            e.printStackTrace();
        }
        return null;
    }
}
