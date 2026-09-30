package br.com.mindtrack.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "analise_ia")
@Getter @Setter @NoArgsConstructor
public class AnaliseIA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "registro_id", unique = true)
    private RegistroHumor registro;

    @ManyToOne(optional = false)
    @JoinColumn(name = "modelo_ia_id")
    private ModeloIA modeloIa;

    @Column(name = "prompt_enviado", length = 8000)
    private String promptEnviado;

    @Column(length = 8000)
    private String resposta;

    private String sentimento;

    @Column(name = "nivel_alerta")
    private String nivelAlerta;

    @Column(name = "data_analise")
    private LocalDateTime dataAnalise = LocalDateTime.now();
}
