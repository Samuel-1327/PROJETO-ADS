package br.com.mindtrack.controller;

import br.com.mindtrack.dto.Dtos.AnaliseResponse;
import br.com.mindtrack.dto.Dtos.RegistroRequest;
import br.com.mindtrack.dto.Dtos.RegistroResponse;
import br.com.mindtrack.service.AnaliseService;
import br.com.mindtrack.service.RegistroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registros")
@RequiredArgsConstructor
public class RegistroController {

    private final RegistroService registroService;
    private final AnaliseService analiseService;

    /** 1) Salvar o registro de humor */
    @PostMapping
    public ResponseEntity<RegistroResponse> criar(@Valid @RequestBody RegistroRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registroService.criar(req));
    }

    /** 2) Enviar para a IA e 3) salvar a resposta */
    @PostMapping("/{id}/analise")
    public AnaliseResponse analisar(@PathVariable Long id) {
        return analiseService.analisar(id);
    }

    /** Consultar o registro com a analise da IA */
    @GetMapping("/{id}")
    public RegistroResponse buscar(@PathVariable Long id) {
        return registroService.buscar(id);
    }
}
