package br.com.mindtrack.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

public class Dtos {

    public record RegistroRequest(
            @NotNull Long usuarioId,
            @Min(1) @Max(5) int nivelHumor,
            @NotBlank @Size(max = 2000) String descricao,
            List<String> emocoes,
            List<String> gatilhos) {
    }

    public record RecomendacaoDto(String tipo, String texto) {
    }

    public record AnaliseResponse(
            Long id,
            String modelo,
            String sentimento,
            String nivelAlerta,
            String resposta,
            List<RecomendacaoDto> recomendacoes,
            String alertaApoio) {
    }

    public record RegistroResponse(
            Long id,
            Long usuarioId,
            int nivelHumor,
            String descricao,
            LocalDateTime dataRegistro,
            List<String> emocoes,
            List<String> gatilhos,
            AnaliseResponse analise) {
    }
}
