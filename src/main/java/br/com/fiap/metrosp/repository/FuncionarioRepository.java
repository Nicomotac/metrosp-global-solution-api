package br.com.fiap.metrosp.repository;
import br.com.fiap.metrosp.model.Funcionario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FuncionarioRepository extends JpaRepository<Funcionario,Long> {
 Optional<Funcionario> findByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCase(String email);
}
