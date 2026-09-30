package br.com.mindtrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recomendacao")
@Getter @Setter @NoArgsConstructor
public class Recomendacao {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "analise_id")
    private AnaliseIA analise;

    private String tipo;

    @Column(length = 2000)
    private String texto;
}
