package com.murilonerdx.gerenciador.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSessionDTO {
    @NotEmpty(message = "Favor informar o nome da Pauta")
    private String name;
    private int minutesOpen = 1;
}
