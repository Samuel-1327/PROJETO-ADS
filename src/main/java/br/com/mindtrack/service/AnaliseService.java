package br.com.mindtrack.service;

import br.com.mindtrack.client.GroqClient;
import br.com.mindtrack.dto.Dtos.AnaliseResponse;
import br.com.mindtrack.dto.Dtos.RecomendacaoDto;
import br.com.mindtrack.model.*;
import br.com.mindtrack.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AnaliseService {

    private static final String PROMPT_SISTEMA = """
            Voce e um assistente de bem-estar emocional de um app de acompanhamento de humor \
            para estudantes e profissionais. Voce NAO faz diagnostico e NAO substitui profissionais de saude. \
            Analise o registro recebido e responda SOMENTE com um JSON valido, em portugues, neste formato: \
            {"sentimento":"positivo|neutro|negativo","nivel_alerta":"baixo|medio|alto",\
            "resumo":"texto curto e acolhedor",\
            "recomendacoes":[{"tipo":"autocuidado|respiracao|sono|social|profissional","texto":"..."}]}. \
            Use nivel_alerta "alto" apenas se houver sinais de sofrimento intenso ou risco; \
            nesse caso inclua uma recomendacao do tipo "profissional". De no maximo 3 recomendacoes.""";

    private static final String MSG_ALERTA = """
            Pelo que voce registrou, pode ser importante conversar com um profissional de saude mental. \
            Se estiver em sofrimento intenso, o CVV atende 24h, gratuitamente, pelo telefone 188.""";

    private final RegistroHumorRepository registroRepo;
    private final AnaliseIARepository analiseRepo;
    private final RecomendacaoRepository recomendacaoRepo;
    private final AlertaApoioRepository alertaRepo;
    private final ModeloIARepository modeloRepo;
    private final GroqClient groq;

    private final ObjectMapper mapper = new ObjectMapper();

    /** Funcionalidade 2 e 3: envia o registro para a IA e salva a resposta. */
    @Transactional
    public AnaliseResponse analisar(Long registroId) {
        RegistroHumor registro = registroRepo.findById(registroId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado"));

        if (analiseRepo.findByRegistroId(registroId).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Este registro ja foi analisado");
        }

        ModeloIA modelo = modeloRepo.findFirstByAtivoTrue()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Nenhum modelo de IA ativo"));

        String prompt = montarPrompt(registro);
        String respostaBruta = groq.perguntar(PROMPT_SISTEMA, prompt);

        JsonNode json;
        try {
            json = mapper.readTree(respostaBruta);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "A IA devolveu uma resposta invalida");
        }

        String nivel = json.path("nivel_alerta").asText("medio").toLowerCase();
        if (!Set.of("baixo", "medio", "alto").contains(nivel)) {
            nivel = "medio";
        }

        AnaliseIA analise = new AnaliseIA();
        analise.setRegistro(registro);
        analise.setModeloIa(modelo);
        analise.setPromptEnviado(prompt);
        analise.setResposta(json.path("resumo").asText(""));
        analise.setSentimento(json.path("sentimento").asText("neutro"));
        analise.setNivelAlerta(nivel);
        analiseRepo.save(analise);

        for (JsonNode r : json.path("recomendacoes")) {
            Recomendacao rec = new Recomendacao();
            rec.setAnalise(analise);
            rec.setTipo(r.path("tipo").asText("autocuidado"));
            rec.setTexto(r.path("texto").asText(""));
            recomendacaoRepo.save(rec);
        }

        if (nivel.equals("alto")) {
            AlertaApoio alerta = new AlertaApoio();
            alerta.setAnalise(analise);
            alerta.setMensagem(MSG_ALERTA);
            alertaRepo.save(alerta);
        }

        return paraResponse(analise);
    }

    @Transactional(readOnly = true)
    public AnaliseResponse buscarPorRegistro(Long registroId) {
        return analiseRepo.findByRegistroId(registroId).map(this::paraResponse).orElse(null);
    }

    private AnaliseResponse paraResponse(AnaliseIA a) {
        List<RecomendacaoDto> recs = new ArrayList<>();
        for (Recomendacao r : recomendacaoRepo.findByAnaliseId(a.getId())) {
            recs.add(new RecomendacaoDto(r.getTipo(), r.getTexto()));
        }
        String alerta = alertaRepo.findByAnaliseId(a.getId()).map(AlertaApoio::getMensagem).orElse(null);
        return new AnaliseResponse(a.getId(), a.getModeloIa().getNomeModelo(), a.getSentimento(),
                a.getNivelAlerta(), a.getResposta(), recs, alerta);
    }

    private String montarPrompt(RegistroHumor r) {
        String emocoes = r.getEmocoes().stream().map(Emocao::getNome).collect(Collectors.joining(", "));
        String gatilhos = r.getGatilhos().stream().map(Gatilho::getNome).collect(Collectors.joining(", "));
        return "Nivel de humor (1 a 5): " + r.getNivelHumor()
                + "\nEmocoes: " + (emocoes.isEmpty() ? "nao informadas" : emocoes)
                + "\nGatilhos: " + (gatilhos.isEmpty() ? "nao informados" : gatilhos)
                + "\nRelato: " + r.getDescricao();
    }
}
