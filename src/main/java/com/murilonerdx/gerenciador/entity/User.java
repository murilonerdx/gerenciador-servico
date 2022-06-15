package com.murilonerdx.gerenciador.entity;

import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/* Classe responsavel pelos usuarios que vão ter acesso a novas pautas*/
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String cpf;

    @Enumerated(EnumType.STRING)
    private StatusVote status;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    private Rulling rulling;
}
