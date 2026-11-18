package br.com.fiap.metrosp.repository;
import br.com.fiap.metrosp.model.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsuarioRepository extends JpaRepository<Usuario,Long> {
 Optional<Usuario> findByEmailIgnoreCase(String email);
 boolean existsByEmailIgnoreCase(String email);
}
