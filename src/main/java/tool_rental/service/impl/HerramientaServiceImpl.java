package tool_rental.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool_rental.exception.ResourceNotFoundException;
import tool_rental.model.Herramienta;
import tool_rental.repository.HerramientaRepository;
import tool_rental.service.HerramientaService;

import java.util.List;

@Service
@Transactional
public class HerramientaServiceImpl implements HerramientaService {

    private final HerramientaRepository herramientaRepository;

    public HerramientaServiceImpl(HerramientaRepository herramientaRepository) {
        this.herramientaRepository = herramientaRepository;
    }

    @Override
    public Herramienta crear(Herramienta herramienta) {
        return herramientaRepository.save(herramienta);
    }

    @Override
    public Herramienta actualizar(Long id, Herramienta herramienta) {
        Herramienta existente = buscarPorId(id);
        existente.setNombre(herramienta.getNombre());
        existente.setCategoria(herramienta.getCategoria());
        existente.setPrecioDia(herramienta.getPrecioDia());
        existente.setStock(herramienta.getStock());
        return herramientaRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Herramienta existente = buscarPorId(id);
        herramientaRepository.delete(existente);
    }

    @Override
    public Herramienta buscarPorId(Long id) {
        return herramientaRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Herramienta no encontrada con id " + id));
    }

    @Override
    public List<Herramienta> listarTodos() {
        return herramientaRepository.findAll();
    }
}