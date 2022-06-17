package com.murilonerdx.gerenciador.jms;

import com.google.gson.Gson;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JmsConsumer {

    private List<User> users = new ArrayList<>();
    private final UserRepository repository;

    @JmsListener( destination = "${activemq.name}" )
    public void listen(String mensagem) {
        User user = new User();
        try {
            System.out.println(mensagem);
            Gson gson = new Gson();
            user = gson.fromJson(mensagem, User.class);
            repository.save(user);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
