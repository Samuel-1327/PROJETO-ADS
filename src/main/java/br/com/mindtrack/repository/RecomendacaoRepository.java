package br.com.mindtrack.repository;

import br.com.mindtrack.model.Recomendacao;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecomendacaoRepository extends JpaRepository<Recomendacao, Long> {
    List<Recomendacao> findByAnaliseId(Long analiseId);
}
