package br.com.mindtrack.config;

import br.com.mindtrack.client.GroqClient;
import br.com.mindtrack.model.ModeloIA;
import br.com.mindtrack.model.Usuario;
import br.com.mindtrack.repository.ModeloIARepository;
import br.com.mindtrack.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Cria um usuario de teste e o modelo de IA ativo quando o banco esta vazio. */
@Component
@RequiredArgsConstructor
public class DadosIniciais implements CommandLineRunner {

    private final UsuarioRepository usuarioRepo;
    private final ModeloIARepository modeloRepo;
    private final GroqClient groq;

    @Override
    public void run(String... args) {
        if (usuarioRepo.count() == 0) {
            Usuario u = new Usuario();
            u.setNome("Usuario Teste");
            u.setEmail("teste@mindtrack.com");
            u.setSenhaHash("nao-usado-no-mvp");
            usuarioRepo.save(u);
        }
        if (modeloRepo.count() == 0) {
            modeloRepo.save(new ModeloIA("Groq", groq.getModel(), true));
        }
    }
}
