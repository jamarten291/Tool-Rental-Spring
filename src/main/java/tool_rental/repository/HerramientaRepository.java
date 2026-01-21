package tool_rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tool_rental.model.Herramienta;

import java.util.List;

@Repository
public interface HerramientaRepository extends JpaRepository<Herramienta, Long> {
    List<Herramienta> findByCategoria(String categoria);
}