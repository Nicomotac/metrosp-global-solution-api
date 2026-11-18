package br.com.fiap.metrosp.repository;
import br.com.fiap.metrosp.model.Ocorrencia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OcorrenciaRepository extends JpaRepository<Ocorrencia,Long> {
 Page<Ocorrencia> findByEstacaoId(Long estacaoId, Pageable pageable);
 boolean existsByEstacaoId(Long estacaoId);
 boolean existsByFuncionarioId(Long funcionarioId);
}
