package com.murilonerdx.gerenciador.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.Gson;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import lombok.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleConsumerDTO {
    private Long id;
    private String name;
    private String email;
    private String cpf;
    private Long idSchedule;
    private int timeExecution;
    Map<String, Integer> scheduleVote = new HashMap<>();

    public String toJson(){
        return new Gson().toJson(this);
    }
}
