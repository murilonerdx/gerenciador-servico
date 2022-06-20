package com.murilonerdx.gerenciador.entity;

import com.murilonerdx.gerenciador.entity.enums.StatusVote;
import lombok.*;

import javax.persistence.*;

/* Classe responsavel pelos usuarios que vão ter acesso a novas pautas*/
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tb_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true, nullable = false)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private StatusVote statusVote;

}
