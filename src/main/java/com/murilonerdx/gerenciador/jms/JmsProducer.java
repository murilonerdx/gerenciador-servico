package com.murilonerdx.gerenciador.jms;

import com.google.gson.Gson;
import com.murilonerdx.gerenciador.dto.ScheduleConsumerDTO;
import com.murilonerdx.gerenciador.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JmsProducer {

    private final JmsTemplate jmsTemplate;

    @Value("${activemq.fila.votacao}")
    private String destinationQueueVotacao;

    public void sendVote(ScheduleConsumerDTO scheduleConsumerDTO){
        jmsTemplate.convertAndSend(destinationQueueVotacao, scheduleConsumerDTO.toJson());
    }
}
