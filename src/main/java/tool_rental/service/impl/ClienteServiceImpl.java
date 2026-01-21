package tool_rental.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tool_rental.exception.ResourceNotFoundException;
import tool_rental.model.Cliente;
import tool_rental.repository.ClienteRepository;
import tool_rental.service.ClienteService;

import java.util.List;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente crear(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long id, Cliente cliente) {
        Cliente existente = buscarPorId(id);
        existente.setNombre(cliente.getNombre());
        existente.setEmail(cliente.getEmail());
        existente.setTelefono(cliente.getTelefono());
        return clienteRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        Cliente existente = buscarPorId(id);
        clienteRepository.delete(existente);
    }

    @Override
    public Cliente buscarPorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
    }

    @Override
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }
}