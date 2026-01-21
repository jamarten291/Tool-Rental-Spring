package tool_rental.service;

import tool_rental.model.Alquiler;

import java.util.List;

public interface AlquilerService {
    Alquiler crear(Alquiler alquiler);
    Alquiler actualizarEstado(Long id, String nuevoEstado);
    void finalizar(Long id);
    void cancelar(Long id);
    Alquiler buscarPorId(Long id);
    List<Alquiler> listarTodos();
    List<Alquiler> listarPorCliente(Long clienteId);
}