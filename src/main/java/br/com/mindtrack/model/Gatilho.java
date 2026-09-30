package br.com.mindtrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "gatilho")
@Getter @Setter @NoArgsConstructor
public class Gatilho {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Gatilho(String nome) {
        this.nome = nome;
    }
}
