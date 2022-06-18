package com.murilonerdx.gerenciador.service;

import com.murilonerdx.gerenciador.dto.ScheduleDTO;
import com.murilonerdx.gerenciador.entity.Schedule;
import com.murilonerdx.gerenciador.entity.User;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import com.murilonerdx.gerenciador.entity.response.ResultResponse;
import com.murilonerdx.gerenciador.exceptions.ScheduleNameException;
import com.murilonerdx.gerenciador.exceptions.ScheduledNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.exceptions.VotacaoFinalizadaException;
import com.murilonerdx.gerenciador.repository.ScheduleRepository;
import com.murilonerdx.gerenciador.repository.UserRepository;
import com.murilonerdx.gerenciador.util.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    ScheduleRepository scheduledRepository;

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
            new Thread(() -> {
                try {
                    Thread.sleep(schedule.getMinutesOpen() * 60000L);

                    schedule.setName(schedule.getName() + " - FINALIZADO - " + LocalDateTime.now());
                    schedule.setActiveStatus(ActiveVote.VF);
                    schedule.setEndSchedule(LocalDateTime.now());
                    List<User> users = scheduledRepository.findById(id).get().getUsers();

                    usersAbleToVote(users);
                    int votes = users.size();
                    schedule.setQtVotes(votes);
                    scheduledRepository.save(schedule);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
            return DozerConverter.parseObject(scheduledRepository.save(schedule), ScheduleDTO.class);
        } else {
            throw new VotacaoFinalizadaException(schedule.getActiveStatus());
        }
    }

    private void usersAbleToVote(List<User> users) {
        users.forEach(x -> {
            x.setSchedule(null);
            x.setStatus(StatusVote.ABLE_TO_VOTE);

            userRepository.save(x);
        });
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

        if (schedule.getActiveStatus().equals(ActiveVote.EV) && user.getSchedule() == null) {
            user.setSchedule(schedule);
            user.setStatus(StatusVote.UNABLE_TO_VOTE);
            schedule.getUsers().add(user);
            scheduledRepository.save(schedule);
        } else if (user.getSchedule() != null) {
            throw new ScheduledNotFoundException("Você já votou, espere a pauta " + schedule.getName() + " terminar para que consiga votar novamente");
        } else {
            throw new VotacaoFinalizadaException(schedule.getActiveStatus());
        }

        return DozerConverter.parseObject(schedule, ScheduleDTO.class);
    }

    public ResultResponse result(Long id) throws ScheduleNameException {
        Schedule schedule = scheduledRepository.findById(id).get();
        LocalDateTime localDateTime = schedule.getStartSchedule().plusMinutes(schedule.getMinutesOpen());

        if (schedule.getActiveStatus().equals(ActiveVote.VF)) {
            ScheduleDTO scheduleDTO = DozerConverter.parseObject(schedule, ScheduleDTO.class);

            return new ResultResponse(scheduleDTO, schedule.getQtVotes());
        }
        throw new ScheduleNameException("A votação ainda não foi finaliza espere até " + localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

    }

    public List<ScheduleDTO> buscarPorTodasSchedules(Pageable pageable) {
        Page<Schedule> schedules = scheduledRepository.findAll(pageable);
        return DozerConverter.parseListObjects(schedules.getContent(), ScheduleDTO.class);
    }

    public List<ScheduleDTO> buscarPorTodasPautasFinalizadas(Pageable pageable) {
        List<Schedule> schedules = scheduledRepository.findAll(pageable).getContent().stream().filter(x ->
                x.getActiveStatus().equals(ActiveVote.AAP) |
                        x.getActiveStatus().equals(ActiveVote.AAP) |
                        x.getActiveStatus().equals(ActiveVote.EV)).collect(Collectors.toList());
        return DozerConverter.parseListObjects(schedules, ScheduleDTO.class);
    }
}
