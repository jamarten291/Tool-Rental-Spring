package tool_rental.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool_rental.exception.ResourceNotFoundException;
import tool_rental.model.Alquiler;
import tool_rental.model.Cliente;
import tool_rental.model.EstadoAlquiler;
import tool_rental.model.Herramienta;
import tool_rental.repository.AlquilerRepository;
import tool_rental.repository.HerramientaRepository;
import tool_rental.service.AlquilerService;
import tool_rental.service.ClienteService;

import java.math.BigDecimal;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;

@Service
@Transactional
public class AlquilerServiceImpl implements AlquilerService {

    private final AlquilerRepository alquilerRepository;
    private final ClienteService clienteService;
    private final HerramientaRepository herramientaRepository;

    public AlquilerServiceImpl(AlquilerRepository alquilerRepository,
                               ClienteService clienteService,
                               HerramientaRepository herramientaRepository) {
        this.alquilerRepository = alquilerRepository;
        this.clienteService = clienteService;
        this.herramientaRepository = herramientaRepository;
    }

    @Override
    public Alquiler crear(Alquiler alquiler) {
        // validar cliente y herramienta
        Long clienteId = alquiler.getCliente() != null ? alquiler.getCliente().getId() : null;
        Long herramientaId = alquiler.getHerramienta() != null ? alquiler.getHerramienta().getId() : null;

        if (clienteId == null || herramientaId == null) {
            throw new IllegalArgumentException("cliente_id y herramienta_id son obligatorios");
        }

        Cliente cliente = clienteService.buscarPorId(clienteId);
        Herramienta herramienta = herramientaRepository.findById(herramientaId)
                .orElseThrow(() -> new ResourceNotFoundException("Herramienta no encontrada con id " + herramientaId));

        if (alquiler.getFechaIni() == null || alquiler.getFechaFin() == null) {
            throw new IllegalArgumentException("Fechas inicio y fin son obligatorias");
        }
        if (alquiler.getFechaFin().isBefore(alquiler.getFechaIni())) {
            throw new IllegalArgumentException("fechaFin debe ser igual o posterior a fechaIni");
        }

        // Comprobar disponibilidad: stock total - cantidad de alquileres activos que solapan con el rango
        List<EstadoAlquiler> activeStates = Arrays.asList(EstadoAlquiler.PENDIENTE, EstadoAlquiler.EN_CURSO);
        long solapantes = alquilerRepository.countOverlappingActive(herramientaId, alquiler.getFechaIni(), alquiler.getFechaFin(), activeStates);
        int stockTotal = herramienta.getStock() == null ? 0 : herramienta.getStock();

        if (solapantes >= stockTotal) {
            throw new IllegalStateException("No hay stock disponible para las fechas solicitadas");
        }

        // No modificamos el stock físico en la base de datos; stock representa el inventario total.
        alquiler.setCliente(cliente);
        alquiler.setHerramienta(herramienta);
        if (alquiler.getEstado() == null) alquiler.setEstado(EstadoAlquiler.PENDIENTE);

        // Inicializar campos financieros
        if (alquiler.getFianza() == null) alquiler.setFianza(BigDecimal.ZERO);
        alquiler.setMulta(BigDecimal.ZERO);
        alquiler.setMontoRetenido(BigDecimal.ZERO);
        alquiler.setMontoDevuelto(BigDecimal.ZERO);

        return alquilerRepository.save(alquiler);
    }

    @Override
    public Alquiler actualizarEstado(Long id, String nuevoEstado) {
        Alquiler existente = buscarPorId(id);
        try {
            EstadoAlquiler estado = EstadoAlquiler.valueOf(nuevoEstado);
            existente.setEstado(estado);
            return alquilerRepository.save(existente);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado);
        }
    }

    @Override
    public void finalizar(Long id) {
        Alquiler existente = buscarPorId(id);
        if (existente.getEstado() == EstadoAlquiler.FINALIZADO) return;

        // Calcular días de retraso (si la fecha actual es posterior a fechaFin)
        LocalDate hoy = LocalDate.now();
        long diasRetraso = 0;
        if (hoy.isAfter(existente.getFechaFin())) {
            diasRetraso = ChronoUnit.DAYS.between(existente.getFechaFin(), hoy);
        }

        BigDecimal multa = BigDecimal.ZERO;
        if (diasRetraso > 0) {
            // multa = 2 * precioDia * diasRetraso
            BigDecimal precioDia = existente.getHerramienta() != null && existente.getHerramienta().getPrecioDia() != null
                    ? existente.getHerramienta().getPrecioDia()
                    : BigDecimal.ZERO;
            multa = precioDia.multiply(BigDecimal.valueOf(2L * diasRetraso));
        }

        existente.setMulta(multa);

        // Determinar monto devuelto de la fianza: fianza - multa (no menor a 0)
        BigDecimal fianza = existente.getFianza() != null ? existente.getFianza() : BigDecimal.ZERO;
        BigDecimal montoDevuelto = fianza.subtract(multa);
        if (montoDevuelto.compareTo(BigDecimal.ZERO) < 0) montoDevuelto = BigDecimal.ZERO;
        existente.setMontoDevuelto(montoDevuelto);

        existente.setEstado(EstadoAlquiler.FINALIZADO);

        alquilerRepository.save(existente);
    }

    @Override
    public void cancelar(Long id) {
        Alquiler existente = buscarPorId(id);
        if (existente.getEstado() == EstadoAlquiler.CANCELADO) return;

        // Calcular horas entre ahora y la fecha de inicio (a partir de la medianoche de fechaIni)
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime inicioAlquilerStart = existente.getFechaIni().atStartOfDay();
        long horasAntesInicio = ChronoUnit.HOURS.between(ahora, inicioAlquilerStart);

        BigDecimal fianza = existente.getFianza() != null ? existente.getFianza() : BigDecimal.ZERO;
        BigDecimal montoRetenido;
        if (horasAntesInicio > 48) {
            // Cancelación gratuita
            montoRetenido = BigDecimal.ZERO;
        } else {
            // Retiene 50% de la fianza
            montoRetenido = fianza.multiply(BigDecimal.valueOf(0.5));
        }

        BigDecimal montoDevuelto = fianza.subtract(montoRetenido);
        if (montoDevuelto.compareTo(BigDecimal.ZERO) < 0) montoDevuelto = BigDecimal.ZERO;

        existente.setMontoRetenido(montoRetenido);
        existente.setMontoDevuelto(montoDevuelto);
        existente.setEstado(EstadoAlquiler.CANCELADO);

        alquilerRepository.save(existente);
    }

    @Override
    public Alquiler buscarPorId(Long id) {
        return alquilerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Alquiler no encontrado con id " + id));
    }

    @Override
    public List<Alquiler> listarTodos() {
        return alquilerRepository.findAll();
    }

    @Override
    public List<Alquiler> listarPorCliente(Long clienteId) {
        return alquilerRepository.findByClienteId(clienteId);
    }
}