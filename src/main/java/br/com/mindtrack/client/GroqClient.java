package br.com.mindtrack.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

/** Conversa com a API da Groq (formato compativel com OpenAI). */
@Component
public class GroqClient {

    private final RestClient rest = RestClient.create();
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${groq.api-key}")
    private String apiKey;

    @Value("${groq.model}")
    private String model;

    @Value("${groq.url}")
    private String url;

    public String getModel() {
        return model;
    }

    /** Envia os prompts e devolve o texto da resposta da IA. */
    public String perguntar(String promptSistema, String promptUsuario) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "Chave da IA nao configurada. Defina a variavel de ambiente GROQ_API_KEY.");
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "temperature", 0.3,
                "response_format", Map.of("type", "json_object"),
                "messages", List.of(
                        Map.of("role", "system", "content", promptSistema),
                        Map.of("role", "user", "content", promptUsuario)));

        try {
            String json = rest.post()
                    .uri(url)
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);

            return mapper.readTree(json)
                    .path("choices").get(0)
                    .path("message").path("content").asText();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                    "Falha ao consultar a IA: " + e.getMessage());
        }
    }
}
