package com.murilonerdx.gerenciador.entity.response;

import com.murilonerdx.gerenciador.dto.ScheduleDTO;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultResponse {
    ScheduleDTO schedule;
    int result;
}
