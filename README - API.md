# MindTrack API (Entrega 3)

Back-end do MindTrack: registro de humor + analise por IA (Groq).
Java 17, Spring Boot 3, Spring Data JPA, banco H2 em memoria.

## Estrutura
- `model/` - 8 classes (Usuario, RegistroHumor, Emocao, Gatilho, ModeloIA, AnaliseIA, Recomendacao, AlertaApoio)
- `repository/` - acesso ao banco
- `service/` - regras (salvar registro, montar prompt, chamar IA, salvar resposta)
- `client/GroqClient` - comunicacao com a API da Groq
- `controller/` - rotas REST

## Como rodar
1. Crie uma chave gratuita em https://console.groq.com
2. Defina a chave como variavel de ambiente (NAO coloque no codigo):
   - Windows (PowerShell): `$env:GROQ_API_KEY="sua-chave"`
   - Linux/Mac: `export GROQ_API_KEY="sua-chave"`
3. Rode: `mvn spring-boot:run` (ou execute `MindTrackApplication` na sua IDE)

## Rotas
| Metodo | Rota | O que faz |
|--------|------|-----------|
| POST | `/registros` | Salva o registro de humor |
| POST | `/registros/{id}/analise` | Envia para a IA e salva a resposta |
| GET | `/registros/{id}` | Mostra o registro com a analise |

## Exemplo
```bash
curl -X POST localhost:8080/registros -H "Content-Type: application/json" -d '{
  "usuarioId": 1,
  "nivelHumor": 2,
  "descricao": "Semana de provas, dormi pouco e estou muito ansioso.",
  "emocoes": ["ansioso", "cansado"],
  "gatilhos": ["provas", "sono ruim"]
}'

curl -X POST localhost:8080/registros/1/analise
curl localhost:8080/registros/1
```

Console do banco: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:mindtrack`, usuario `sa`, senha vazia)

## Observacoes
- O usuario de teste (id 1) e criado automaticamente ao iniciar.
- O nome do modelo da IA fica em `application.properties` (`groq.model`); se a Groq aposentar o modelo, troque pelo atual listado no console.
- O app nao faz diagnostico: em caso de alerta alto, mostra orientacao para buscar profissional e o CVV (188).
