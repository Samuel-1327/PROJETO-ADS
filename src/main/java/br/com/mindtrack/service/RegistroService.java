package br.com.mindtrack.service;

import br.com.mindtrack.dto.Dtos.RegistroRequest;
import br.com.mindtrack.dto.Dtos.RegistroResponse;
import br.com.mindtrack.model.*;
import br.com.mindtrack.repository.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RegistroService {

    private final RegistroHumorRepository registroRepo;
    private final UsuarioRepository usuarioRepo;
    private final EmocaoRepository emocaoRepo;
    private final GatilhoRepository gatilhoRepo;
    private final AnaliseService analiseService;

    /** Funcionalidade 1: cadastra o registro de humor (entidade principal). */
    @Transactional
    public RegistroResponse criar(RegistroRequest req) {
        Usuario usuario = usuarioRepo.findById(req.usuarioId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario nao encontrado"));

        RegistroHumor registro = new RegistroHumor();
        registro.setUsuario(usuario);
        registro.setNivelHumor(req.nivelHumor());
        registro.setDescricao(req.descricao());

        if (req.emocoes() != null) {
            for (String nome : req.emocoes()) {
                String n = nome.trim().toLowerCase();
                if (!n.isEmpty()) {
                    registro.getEmocoes().add(emocaoRepo.findByNomeIgnoreCase(n)
                            .orElseGet(() -> emocaoRepo.save(new Emocao(n))));
                }
            }
        }
        if (req.gatilhos() != null) {
            for (String nome : req.gatilhos()) {
                String n = nome.trim().toLowerCase();
                if (!n.isEmpty()) {
                    registro.getGatilhos().add(gatilhoRepo.findByNomeIgnoreCase(n)
                            .orElseGet(() -> gatilhoRepo.save(new Gatilho(n))));
                }
            }
        }

        registroRepo.save(registro);
        return paraResponse(registro);
    }

    @Transactional(readOnly = true)
    public RegistroResponse buscar(Long id) {
        RegistroHumor registro = registroRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Registro nao encontrado"));
        return paraResponse(registro);
    }

    private RegistroResponse paraResponse(RegistroHumor r) {
        List<String> emocoes = r.getEmocoes().stream().map(Emocao::getNome).sorted().toList();
        List<String> gatilhos = r.getGatilhos().stream().map(Gatilho::getNome).sorted().toList();
        return new RegistroResponse(r.getId(), r.getUsuario().getId(), r.getNivelHumor(), r.getDescricao(),
                r.getDataRegistro(), emocoes, gatilhos, analiseService.buscarPorRegistro(r.getId()));
    }
}
