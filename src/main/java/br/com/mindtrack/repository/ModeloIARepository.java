package br.com.mindtrack.repository;

import br.com.mindtrack.model.ModeloIA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModeloIARepository extends JpaRepository<ModeloIA, Long> {
    Optional<ModeloIA> findFirstByAtivoTrue();
}
