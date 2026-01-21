package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.model.Alquiler;
import tool_rental.service.AlquilerService;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) { this.alquilerService = alquilerService; }

    @GetMapping
    public List<Alquiler> listar() { return alquilerService.listarTodos(); }

    @GetMapping("/{id}")
    public Alquiler obtener(@PathVariable Long id) { return alquilerService.buscarPorId(id); }

    @GetMapping("/cliente/{clienteId}")
    public List<Alquiler> listarPorCliente(@PathVariable Long clienteId) { return alquilerService.listarPorCliente(clienteId); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Alquiler crear(@Valid @RequestBody Alquiler alquiler) { return alquilerService.crear(alquiler); }

    @PutMapping("/{id}/estado")
    public Alquiler actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        return alquilerService.actualizarEstado(id, estado);
    }

    @PostMapping("/{id}/finalizar")
    public void finalizar(@PathVariable Long id) {
        alquilerService.finalizar(id);
    }

    @PostMapping("/{id}/cancelar")
    public Alquiler cancelar(@PathVariable Long id) {
        alquilerService.cancelar(id);
        return alquilerService.buscarPorId(id);
    }
}