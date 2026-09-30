package br.com.mindtrack.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "modelo_ia")
@Getter @Setter @NoArgsConstructor
public class ModeloIA {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String provedor;

    @Column(name = "nome_modelo", nullable = false)
    private String nomeModelo;

    private boolean ativo = true;

    public ModeloIA(String provedor, String nomeModelo, boolean ativo) {
        this.provedor = provedor;
        this.nomeModelo = nomeModelo;
        this.ativo = ativo;
    }
}
