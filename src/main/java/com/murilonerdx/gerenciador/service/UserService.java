package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.entity.request.UserRequest;
import com.murilonerdx.gerenciador.entity.response.StatusResponse;
import com.murilonerdx.gerenciador.exceptions.CpfNotFoundException;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.repository.VoteRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {
    Logger logger = LogManager.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> listarTodosUsuarios(Pageable pageable) {
        Page<User> all = userRepository.findAll(pageable);
        return DozerConverter.parseListObjects(all.getContent(), UserDTO.class);
    }

    public UserDTO criarUsuario(UserRequest userDTO) {
        try {
            User user = DozerConverter.parseObject(userDTO, User.class);
            user.setStatusVote(StatusVote.ABLE_TO_VOTE);
            logger.info("Criando um novo usuario  "+ user.getName());
            return DozerConverter.parseObject(userRepository.save(user), UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Digite um email/CPF que não esteja cadastrado");
        }
    }

    public UserDTO procurarPorEmail(String email) throws EmailNotFoundException {
        try {
            logger.info("Procurando e-mail:  "+ email);
            return DozerConverter.parseObject(userRepository.findByEmail(email).get(), UserDTO.class);
        } catch (Exception e) {
            logger.error("Erro encontrado: "+ e.getMessage());
            throw new EmailNotFoundException(email);
        }
    }

    public void deletarUsuario(Long id) {
        try {
            User user = userRepository.findById(id)
                    .orElseThrow(() ->
                            new UserNotFoundException("Usuario com id: " + id + " não encontrado"));

            logger.info("Deletando Usuario:  "+ user.getName());
            userRepository.delete(user);
        } catch (UserNotFoundException e) {
            logger.error("Erro encontrado:  "+ e.getMessage());
            e.printStackTrace();
        }
    }


    public UserDTO atualizarUsuario(Long id, UserRequest userDTO) {
        try {
            User oldUser = userRepository.findById(id).orElseThrow(() ->
                    new UserNotFoundException("Usuario com id: " + id + " não encontrado"));

            User newUser = DozerConverter.parseObject(userDTO, User.class);
            newUser.setId(oldUser.getId());

            logger.info("Atualizando usuario:  "+ oldUser.getName());
            return DozerConverter.parseObject(userRepository.save(oldUser), UserDTO.class);
        } catch (UserNotFoundException e) {
            logger.error("Erro encontrado:  "+e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public UserDTO buscarPorId(Long id) {
        return DozerConverter.parseObject(userRepository.findById(id).get(), UserDTO.class);
    }

    public StatusResponse buscarCPF(String cpf) throws CpfNotFoundException {
        User byCpf = userRepository.findByCpf(cpf).orElseThrow(() -> new CpfNotFoundException(cpf));
        return new StatusResponse(byCpf.getStatusVote());
    }
}
