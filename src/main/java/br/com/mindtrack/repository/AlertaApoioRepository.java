package br.com.mindtrack.repository;

import br.com.mindtrack.model.AlertaApoio;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaApoioRepository extends JpaRepository<AlertaApoio, Long> {
    Optional<AlertaApoio> findByAnaliseId(Long analiseId);
}
