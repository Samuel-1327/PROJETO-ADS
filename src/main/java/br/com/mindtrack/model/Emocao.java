package br.com.mindtrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "emocao")
@Getter @Setter @NoArgsConstructor
public class Emocao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nome;

    public Emocao(String nome) {
        this.nome = nome;
    }
}
