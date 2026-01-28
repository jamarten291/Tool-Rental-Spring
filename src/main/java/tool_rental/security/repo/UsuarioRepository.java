package tool_rental.security.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import tool_rental.security.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
}
