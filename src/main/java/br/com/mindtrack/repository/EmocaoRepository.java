package br.com.mindtrack.repository;

import br.com.mindtrack.model.Emocao;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmocaoRepository extends JpaRepository<Emocao, Long> {
    Optional<Emocao> findByNomeIgnoreCase(String nome);
}
