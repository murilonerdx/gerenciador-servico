package com.murilonerdx.gerenciador.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/* Classe responsavel pela pauta referenciada*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rulling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private int hoursOpen = 1;
    private int qtVotes;
    private boolean isRullingOpen;
}
