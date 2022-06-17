package com.murilonerdx.gerenciador.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDTO {
    private Long id;
    private String name;
    private int minutesOpen = 1;
    private ActiveVote activeStatus;

}
