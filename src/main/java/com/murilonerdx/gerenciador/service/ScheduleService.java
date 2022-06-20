package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.ScheduleDTO;
import com.murilonerdx.gerenciador.entity.Schedule;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.Vote;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.entity.response.ResultResponse;
import com.murilonerdx.gerenciador.exceptions.ScheduleNameException;
import com.murilonerdx.gerenciador.exceptions.ScheduledNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.exceptions.VotacaoFinalizadaException;
import com.murilonerdx.gerenciador.repository.ScheduleRepository;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.repository.VoteRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduledRepository;

    @Autowired
    VoteRepository voteRepository;

    @Autowired
    private UserRepository userRepository;

    public ScheduleDTO criarNovaPauta(ScheduleDTO scheduleDTO) throws ScheduleNameException {
        Schedule scheduleSearch = scheduledRepository.findByName(scheduleDTO.getName());

        if (scheduleSearch == null) {
            if (scheduleDTO.getMinutesOpen() <= 0) scheduleDTO.setMinutesOpen(1);

            scheduleDTO.setId(null);
            scheduleDTO.setActiveStatus(ActiveVote.AAP);

            Schedule schedule = DozerConverter.parseObject(scheduleDTO, Schedule.class);

            return DozerConverter.parseObject(scheduledRepository.save(schedule), ScheduleDTO.class);
        } else {
            throw new ScheduleNameException("Nome de pauta já cadastrado, digite um nome não cadastrado.");
        }
    }

    public ScheduleDTO buscarPorId(Long id) {
        return DozerConverter.parseObject(scheduledRepository.findById(id).get(), ScheduleDTO.class);
    }

    public ScheduleDTO sessaoNovaPauta(Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException {
        Schedule schedule = scheduledRepository.findById(id).orElseThrow(() -> new ScheduledNotFoundException("Scheduled com id: " + id + " não existe!"));

        if (schedule.getActiveStatus().equals(ActiveVote.AIS)) {
            schedule.setActiveStatus(ActiveVote.EV);
            schedule.setStartSchedule(LocalDateTime.now());
            threadPending(schedule);

            scheduledRepository.save(schedule);
            return DozerConverter.parseObject(scheduledRepository.save(schedule), ScheduleDTO.class);
        } else {
            throw new VotacaoFinalizadaException(schedule.getActiveStatus());
        }
    }

    private void threadPending(Schedule schedule) {
        new Thread(() -> {
            try {
                Thread.sleep(schedule.getMinutesOpen() * 60000L);
                schedule.setName(schedule.getName() + " - FINALIZADO - " + LocalDateTime.now());
                schedule.setActiveStatus(ActiveVote.VF);
                schedule.setEndSchedule(LocalDateTime.now());

                List<User> users = validUsersVote(schedule);
                userRepository.saveAll(users);

                List<Vote> bySchedule = voteRepository.findBySchedule(schedule);

                getVotesAndSave(schedule, bySchedule);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void getVotesAndSave(Schedule schedule, List<Vote> bySchedule) {
        schedule.setQtVotes(bySchedule.size());
        scheduledRepository.save(schedule);
    }

    public List<User> validUsersVote(Schedule schedule) {
        List<User> users = new ArrayList<>();
        List<Vote> votes = voteRepository.findBySchedule(schedule);
        for(Vote vote : votes){
            User user = vote.getUser();
            user.setStatusVote(StatusVote.ABLE_TO_VOTE);
            users.add(user);
        }
        return users;
    }

    public ScheduleDTO abrirPauta(Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException {
        Schedule schedule = scheduledRepository.findById(id).orElseThrow(() -> new ScheduledNotFoundException("Scheduled com id: " + id + " não existe!"));

        if (schedule.getActiveStatus().equals(ActiveVote.AAP)) {
            schedule.setActiveStatus(ActiveVote.AIS);
            return DozerConverter.parseObject(scheduledRepository.save(schedule), ScheduleDTO.class);
        } else {
            throw new VotacaoFinalizadaException(schedule.getActiveStatus());
        }
    }

    public ScheduleDTO votar(Long id, Long idUser) throws ScheduledNotFoundException, VotacaoFinalizadaException, UserNotFoundException {
        Schedule schedule = scheduledRepository.findById(id).orElseThrow(() -> new ScheduledNotFoundException("Scheduled com id: " + id + " não existe!"));
        User user = userRepository.findById(idUser).orElseThrow(() -> new UserNotFoundException("Usuario id: " + idUser + " não está cadastrado"));

        Optional<Vote> byUser = voteRepository.findByScheduleAndUser(schedule, user);
        if (schedule.getActiveStatus().equals(ActiveVote.EV)
                && byUser.isEmpty() && (user.getStatusVote().equals(StatusVote.ABLE_TO_VOTE))) {
            Vote vote = new Vote();
            vote.setUser(user);
            vote.setSchedule(schedule);

            user.setStatusVote(StatusVote.UNABLE_TO_VOTE);
            userRepository.save(user);

            voteRepository.save(vote);
        } else if (user.getStatusVote().equals(StatusVote.UNABLE_TO_VOTE)) {
            throw new ScheduledNotFoundException("Você já votou, espere a pauta " + schedule.getName() + " terminar para que consiga votar novamente");
        } else {
            throw new VotacaoFinalizadaException(schedule.getActiveStatus());
        }

        return DozerConverter.parseObject(schedule, ScheduleDTO.class);
    }

    public ResultResponse result(Long id) throws ScheduleNameException {
        Schedule schedule = scheduledRepository.findById(id).orElseThrow(() -> new ScheduleNameException("Essa pauta não existe"));
        if (schedule.getStartSchedule() != null) {
            LocalDateTime localDateTime = schedule.getStartSchedule().plusMinutes(schedule.getMinutesOpen());

            if (schedule.getActiveStatus().equals(ActiveVote.VF) && schedule.getEndSchedule() != null) {
                ScheduleDTO scheduleDTO = DozerConverter.parseObject(schedule, ScheduleDTO.class);

                return new ResultResponse(scheduleDTO, schedule.getQtVotes());
            } else if(schedule.getEndSchedule() == null && schedule.getActiveStatus().equals(ActiveVote.EV) ){
                throw new ScheduleNameException("A votação ainda não foi finaliza espere até " + localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
            }
        }
        throw new ScheduleNameException("A votação está sendo computada aguarde ");
    }

    @CacheEvict(value = "schedules", allEntries = true)
    public List<ScheduleDTO> buscarPorTodasSchedules(Pageable pageable) {
        Page<Schedule> schedules = scheduledRepository.findAll(pageable);
        return DozerConverter.parseListObjects(schedules.getContent(), ScheduleDTO.class);
    }

    @CacheEvict(value = "schedulesFinished", allEntries = true)
    public List<ScheduleDTO> buscarPorTodasPautasFinalizadas(Pageable pageable) {
        List<Schedule> schedules = scheduledRepository.findAll(pageable).getContent().stream().filter(x ->
                x.getActiveStatus().equals(ActiveVote.AAP) |
                        x.getActiveStatus().equals(ActiveVote.AAP) |
                        x.getActiveStatus().equals(ActiveVote.EV)).collect(Collectors.toList());
        return DozerConverter.parseListObjects(schedules, ScheduleDTO.class);
    }

}
