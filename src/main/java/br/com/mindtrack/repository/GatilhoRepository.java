package br.com.mindtrack.repository;

import br.com.mindtrack.model.Gatilho;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GatilhoRepository extends JpaRepository<Gatilho, Long> {
    Optional<Gatilho> findByNomeIgnoreCase(String nome);
}
