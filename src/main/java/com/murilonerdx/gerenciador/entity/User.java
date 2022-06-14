package com.murilonerdx.gerenciador.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/* Classe responsavel pelos usuarios que vão ter acesso a novas pautas*/
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_rulling", joinColumns = {
            @JoinColumn(name = "id_user")
    }, inverseJoinColumns = {
            @JoinColumn(name = "id_rulling")
    })
    private List<Rulling> rulling;
}
