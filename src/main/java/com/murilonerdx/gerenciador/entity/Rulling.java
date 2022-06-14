package com.murilonerdx.gerenciador.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

/* Classe responsavel pela pauta referenciada*/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Rulling {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qtVotes;
    private boolean isRullingOpen;
}
