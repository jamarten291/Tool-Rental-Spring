package tool_rental.service;

import tool_rental.model.Cliente;

import java.util.List;

public interface ClienteService {
    Cliente crear(Cliente cliente);
    Cliente actualizar(Long id, Cliente cliente);
    void eliminar(Long id);
    Cliente buscarPorId(Long id);
    List<Cliente> listarTodos();
}