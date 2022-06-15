package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.entity.response.StatusResponse;
import com.murilonerdx.gerenciador.exceptions.CpfNotFoundException;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public List<UserDTO> listarTodosUsuarios() {
        return DozerConverter.parseListObjects(repository.findAll(), UserDTO.class);
    }

    public UserDTO criarUsuario(UserRequestDTO userDTO) {
        try{
            User user = DozerConverter.parseObject(userDTO, User.class);
            user.setStatus(StatusVote.ABLE_TO_VOTE);
            return DozerConverter.parseObject(repository.save(user), UserDTO.class);
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Digite um email que não esteja cadastrado, e-mail: " + userDTO.getEmail() + " já está cadastrado");
        }
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
                            new UserNotFoundException("Usuario com id: " + id + " não encontrado"));
            repository.delete(user);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
    }


    public UserDTO atualizarUsuario(Long id, UserRequestDTO userDTO) {
        try {
            User newUser = repository.findById(id).orElseThrow(()->
                    new UserNotFoundException("Usuario com id: " + id + " não encontrado"));

            User oldUser = DozerConverter.parseObject(userDTO, User.class);
            oldUser.setId(newUser.getId());

            return DozerConverter.parseObject(repository.save(oldUser), UserDTO.class);
        } catch (UserNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public UserDTO buscarPorId(Long id) {
        return DozerConverter.parseObject(repository.findById(id), UserDTO.class);
    }

    public StatusResponse buscarCPF(String cpf) throws CpfNotFoundException {
        User byCpf = repository.findByCpf(cpf).orElseThrow(()-> new CpfNotFoundException(cpf));
        return new StatusResponse(byCpf.getStatus());
    }
}
