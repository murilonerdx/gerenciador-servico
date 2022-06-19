package com.murilonerdx.gerenciador.jms;

import com.google.gson.Gson;
import com.murilonerdx.gerenciador.dto.ScheduleConsumerDTO;
import com.murilonerdx.gerenciador.entity.Schedule;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.Vote;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.repository.ScheduleRepository;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.repository.VoteRepository;
import com.murilonerdx.gerenciador.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class JmsConsumer {

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    ScheduleRepository scheduledRepository;

    @Autowired
    ScheduleService scheduleService;

    @Autowired
    private UserRepository userRepository;

    @JmsListener( destination = "${activemq.fila.votacao}")
    public void listenVote(String voteDTO) {
        try {
            Gson gson = new Gson();

            ScheduleConsumerDTO vote = gson.fromJson(voteDTO, ScheduleConsumerDTO.class);
            Optional<Schedule> schedule = scheduledRepository.findById(vote.getIdSchedule());
            Optional<User> user = userRepository.findByCpf(vote.getCpf());

            Vote voto = new Vote();
            voto.setUser(user.get());
            voto.setSchedule(schedule.get());
            voteRepository.save(voto);


            schedule.get().setEndSchedule(LocalDateTime.now());
            List<User> users = scheduleService.validUsersVote(schedule.get());
            userRepository.saveAll(users);

            List<Vote> bySchedule = voteRepository.findBySchedule(schedule.get());

            schedule.get().setQtVotes(bySchedule.size());
            scheduledRepository.save(schedule.get());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
