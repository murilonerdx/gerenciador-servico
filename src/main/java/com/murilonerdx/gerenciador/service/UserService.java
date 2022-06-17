package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.UserDTO;
import com.murilonerdx.gerenciador.entity.Permission;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.entity.request.UserRequestDTO;
import com.murilonerdx.gerenciador.entity.response.StatusResponse;
import com.murilonerdx.gerenciador.exceptions.CpfNotFoundException;
import com.murilonerdx.gerenciador.exceptions.EmailNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
//import com.murilonerdx.gerenciador.jms.JmsConsumer;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService implements UserDetailsService {
    Logger logger = LogManager.getLogger(UserService.class);

//    @Autowired
//    JmsTemplate jmsTemplate;

//    @Autowired
//    JmsConsumer jmsConsumer;

    @Autowired
    private UserRepository repository;

    public List<UserDTO> listarTodosUsuarios(Pageable pageable) {
        Page<User> all = repository.findAll(pageable);
        return DozerConverter.parseListObjects(all.getContent(), UserDTO.class);
    }

    public UserDTO criarUsuario(UserRequestDTO userDTO) {
        try {
//            jmsTemplate.convertAndSend("fila.votacao.resultado", new Gson().toJson(userDTO));
//            Integer totalPendingMessages = this.jmsTemplate.browse("fila.votacao.resultado", (session, browser) -> Collections.list(browser.getEnumeration()).size());
//            int i = totalPendingMessages == null ? 0 : totalPendingMessages;
//
//            System.out.println(i);

            User user = DozerConverter.parseObject(userDTO, User.class);
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            user.setStatus(StatusVote.ABLE_TO_VOTE);
            user.getPermissions().add(new Permission(1L, "ROLE_USER"));
            logger.info("Criando um novo usuario "+ user.getId());
            return DozerConverter.parseObject(repository.save(user), UserDTO.class);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException("Digite um email/CPF que não esteja cadastrado");
        }
    }

    public UserDTO procurarPorEmail(String email) throws EmailNotFoundException {
        try {
            logger.info("Procurando e-mail: "+ email);
            return DozerConverter.parseObject(repository.findByEmail(email).get(), UserDTO.class);
        } catch (Exception e) {
            logger.error("Erro encontrado: "+ e.getMessage());
            throw new EmailNotFoundException(email);
        }
    }

    public void deletarUsuario(Long id) {
        try {
            User user = repository.findById(id)
                    .orElseThrow(() ->
                            new UserNotFoundException("Usuario com id: " + id + " não encontrado"));

            logger.info("Deletando Usuario: "+ user.getName());
            repository.delete(user);
        } catch (UserNotFoundException e) {
            logger.error("Erro encontrado: "+ e.getMessage());
            e.printStackTrace();
        }
    }


    public UserDTO atualizarUsuario(Long id, UserRequestDTO userDTO) {
        try {
            User oldUser = repository.findById(id).orElseThrow(() ->
                    new UserNotFoundException("Usuario com id: " + id + " não encontrado"));

            User newUser = DozerConverter.parseObject(userDTO, User.class);
            newUser.setId(oldUser.getId());
            newUser.setStatus(oldUser.getStatus());

            logger.info("Atualizando usuario: "+ oldUser.getName());
            return DozerConverter.parseObject(repository.save(oldUser), UserDTO.class);
        } catch (UserNotFoundException e) {
            logger.error("Erro encontrado: "+ e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public UserDTO buscarPorId(Long id) {
        return DozerConverter.parseObject(repository.findById(id).get(), UserDTO.class);
    }

    public StatusResponse buscarCPF(String cpf) throws CpfNotFoundException {
        User byCpf = repository.findByCpf(cpf).orElseThrow(() -> new CpfNotFoundException(cpf));
        return new StatusResponse(byCpf.getStatus());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = repository.findByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("e-mail not found");
        return user.get();
    }
}
