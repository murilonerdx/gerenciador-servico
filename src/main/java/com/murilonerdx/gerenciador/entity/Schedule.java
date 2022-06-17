package com.murilonerdx.gerenciador.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.murilonerdx.gerenciador.entity.enums.ActiveVote;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/* Classe responsavel pela pauta referenciada*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique=true)
    private String name;
    private int minutesOpen = 1;
    private int qtVotes;

    private LocalDateTime startSchedule;
    private LocalDateTime endSchedule;

    @Enumerated(EnumType.STRING)
    private ActiveVote activeStatus;

    @OneToMany(mappedBy="schedule", fetch=FetchType.EAGER)
    private List<User> users = new ArrayList<>();
}
