//package com.murilonerdx.gerenciador.jms;
//
//import com.google.gson.Gson;
//import com.murilonerdx.gerenciador.entity.User;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.jms.core.JmsTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class JmsProducer {
//
//    private final JmsTemplate jmsTemplate;
//
//    @Value("${activemq.name}")
//    private String destinationQueue;
//
//    public void send(User user){
//        Gson gson = new Gson();
//        String jsonPerson = gson.toJson(user);
//        jmsTemplate.convertAndSend(destinationQueue, jsonPerson);
//    }
//
//}
