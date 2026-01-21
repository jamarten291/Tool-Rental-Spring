package tool_rental.service;

import tool_rental.model.Herramienta;

import java.util.List;

public interface HerramientaService {
    Herramienta crear(Herramienta herramienta);
    Herramienta actualizar(Long id, Herramienta herramienta);
    void eliminar(Long id);
    Herramienta buscarPorId(Long id);
    List<Herramienta> listarTodos();
}