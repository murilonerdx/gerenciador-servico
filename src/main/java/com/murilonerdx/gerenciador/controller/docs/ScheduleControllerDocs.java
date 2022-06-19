package com.murilonerdx.gerenciador.controller.docs;

import com.murilonerdx.gerenciador.dto.ScheduleDTO;
import com.murilonerdx.gerenciador.dto.ScheduleSessionDTO;
import com.murilonerdx.gerenciador.entity.response.VoteSessionRequest;
import com.murilonerdx.gerenciador.exceptions.ScheduleNameException;
import com.murilonerdx.gerenciador.exceptions.ScheduledNotFoundException;
import com.murilonerdx.gerenciador.exceptions.UserNotFoundException;
import com.murilonerdx.gerenciador.exceptions.VotacaoFinalizadaException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jms.JMSException;
import javax.naming.NamingException;
import java.util.List;

@Api(tags="Schedule management")
public interface ScheduleControllerDocs {
    @ApiOperation(value = "Schedule creation operation")
    @ApiResponses(
            value =
                    {
                            @ApiResponse(code = 201, message = "Success schedule creation")
                            , @ApiResponse(
                            code = 400,
                            message =
                                    "Missing required fields, wrong field range value or schedule already registered on system")
                    })
    ResponseEntity<ScheduleDTO>
    create(ScheduleSessionDTO scheduleDTO) throws ScheduleNameException;

    @ApiOperation(value = "List all registered schedules finish")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200,
                            message = "Return all registered schedules finished")
            })
    ResponseEntity<List<ScheduleDTO>>
    findAllFinish(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    );

    @ApiOperation(value = "List all registered schedules")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 200,
                            message = "Return all registered schedules")
            })
    ResponseEntity<List<ScheduleDTO>>
    findAll(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "12") int limit,
            @RequestParam(value="direction", defaultValue = "asc") String direction
    );

    @ApiOperation(value = "Find schedule by id operation")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 204, message = "Success schedule find")
                    , @ApiResponse(code = 404,
                    message = "\"Schedule not found error code")
            })
    ResponseEntity<ScheduleDTO>
    findById(Long id);


    @ApiOperation(value = "Vote in schedule operation")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 204, message = "Success schedule vote")
                    , @ApiResponse(code = 404,
                    message = "\"Schedule not found error code")
            })
    ResponseEntity<?> vote(Long id, Long idUser) throws UserNotFoundException, ScheduledNotFoundException, VotacaoFinalizadaException, NamingException, JMSException;


    @ApiOperation(value = "Open schedule operation")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 204, message = "Success schedule opened")
                    , @ApiResponse(code = 404,
                    message = "\"Schedule not found error code")
            })
    ResponseEntity<?> openSchedule(Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException, NamingException, JMSException;

    @ApiOperation(value = "Open session schedule operation")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 204, message = "Success schedule session opened")
                    , @ApiResponse(code = 404,
                    message = "\"Schedule not found error code")
            })
    ResponseEntity<?> openSessionVote(Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException;

    @ApiOperation(value = "Open result schedule operation")
    @ApiResponses(value =
            {
                    @ApiResponse(code = 204, message = "Success schedule result")
                    , @ApiResponse(code = 404,
                    message = "\"Schedule not found error code")
            })
    ResponseEntity<?> resultSchedule(Long id) throws ScheduledNotFoundException, VotacaoFinalizadaException, ScheduleNameException;
}
