package com.murilonerdx.gerenciador.controller;

import com.murilonerdx.gerenciador.controller.docs.ScheduleControllerDocs;
import com.murilonerdx.gerenciador.dto.ScheduleDTO;
import com.murilonerdx.gerenciador.dto.ScheduleSessionDTO;
import com.murilonerdx.gerenciador.entity.response.VoteSessionRequest;
import com.murilonerdx.gerenciador.exceptions.ScheduleNameException;
import com.murilonerdx.gerenciador.exceptions.ScheduledNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.exceptions.VotacaoFinalizadaException;
import com.murilonerdx.gerenciador.service.ScheduleService;
import com.murilonerdx.gerenciador.util.DozerConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.List;

@RestController
@RequestMapping(value = "/v1/api/schedule")
public class ScheduleController implements ScheduleControllerDocs {

    @Autowired
    ScheduleService service;

    @Override
    @PostMapping(value="/", produces = {"application/json", "application/xml", "application/x-yaml"},
            consumes = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ScheduleDTO> create(@RequestBody ScheduleSessionDTO scheduleSessionDTO) throws ScheduleNameException {
        ScheduleDTO scheduleDTO = DozerConverter.parseObject(scheduleSessionDTO, ScheduleDTO.class);
        return ResponseEntity.ok().body(service.criarNovaPauta(scheduleDTO));
    }

    @Override
    @GetMapping(value="/", produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<List<ScheduleDTO>> findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok().body(service.buscarPorTodasPautasFinalizadas(pageable));
    }

    @Override
    @GetMapping(value="/finish", produces = { "application/json", "application/xml", "application/x-yaml" })
    public ResponseEntity<List<ScheduleDTO>> findAllFinish(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    ) {
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "name"));

        return ResponseEntity.ok().body(service.buscarPorTodasSchedules(pageable));
    }

    @Override
    @GetMapping(value = "/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<ScheduleDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @Override
    @PostMapping(value = "/vote/{idSchedule}/user/{idUser}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> vote(@PathVariable("idSchedule") Long idSchedule, @PathVariable("idUser") Long idUser) throws UserNotFoundException, ScheduledNotFoundException, VotacaoFinalizadaException, NamingException, JMSException {
        service.votar(idSchedule, idUser);

        return ResponseEntity.ok().body("Seu voto foi computado");
    }

    @Override
    @GetMapping(value = "/open-schedule/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> openSchedule(@PathVariable("id") Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException, NamingException, JMSException {
        return ResponseEntity.ok().body(service.abrirPauta(id));
    }

    @Override
    @GetMapping(value = "/open-session/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> openSessionVote(@PathVariable("id") Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException {
        return ResponseEntity.ok().body(service.sessaoNovaPauta(id));
    }

    @Override
    @GetMapping(value = "/result/{id}", produces = {"application/json", "application/xml", "application/x-yaml"})
    public ResponseEntity<?> resultSchedule(Long id) throws ScheduleNameException {
        return ResponseEntity.ok().body(service.result(id));
    }
}
