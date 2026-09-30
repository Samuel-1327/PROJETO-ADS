package br.com.mindtrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "registro_humor")
@Getter @Setter @NoArgsConstructor
public class RegistroHumor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(name = "nivel_humor", nullable = false)
    private int nivelHumor;

    @Column(length = 2000)
    private String descricao;

    @Column(name = "data_registro")
    private LocalDateTime dataRegistro = LocalDateTime.now();

    @ManyToMany
    @JoinTable(name = "registro_emocao",
            joinColumns = @JoinColumn(name = "registro_id"),
            inverseJoinColumns = @JoinColumn(name = "emocao_id"))
    private Set<Emocao> emocoes = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "registro_gatilho",
            joinColumns = @JoinColumn(name = "registro_id"),
            inverseJoinColumns = @JoinColumn(name = "gatilho_id"))
    private Set<Gatilho> gatilhos = new HashSet<>();
}
