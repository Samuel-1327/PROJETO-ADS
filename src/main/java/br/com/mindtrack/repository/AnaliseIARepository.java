package br.com.mindtrack.repository;

import br.com.mindtrack.model.AnaliseIA;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnaliseIARepository extends JpaRepository<AnaliseIA, Long> {
    Optional<AnaliseIA> findByRegistroId(Long registroId);
}
