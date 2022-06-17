package com.murilonerdx.gerenciador.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleSessionDTO {
    private String name;
    private int minutesOpen = 1;
}
