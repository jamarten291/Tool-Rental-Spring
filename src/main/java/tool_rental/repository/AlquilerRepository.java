package tool_rental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tool_rental.model.Alquiler;
import tool_rental.model.EstadoAlquiler;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long> {
    List<Alquiler> findByClienteId(Long clienteId);
    List<Alquiler> findByHerramientaId(Long herramientaId);

    /**
     * Cuenta alquileres activos (por ejemplo PENDIENTE y EN_CURSO) que solapan con el rango [fechaIni, fechaFin].
     * Solapamiento: NOT (a.fechaFin < fechaIni OR a.fechaIni > fechaFin)
     */
    @Query("SELECT COUNT(a) FROM Alquiler a " +
            "WHERE a.herramienta.id = :hid " +
            "AND a.estado IN :states " +
            "AND NOT (a.fechaFin < :fechaIni OR a.fechaIni > :fechaFin)")
    long countOverlappingActive(@Param("hid") Long herramientaId,
                                @Param("fechaIni") LocalDate fechaIni,
                                @Param("fechaFin") LocalDate fechaFin,
                                @Param("states") List<EstadoAlquiler> states);
}