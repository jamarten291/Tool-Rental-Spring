package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.model.Cliente;
import tool_rental.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) { this.clienteService = clienteService; }

    @GetMapping
    public List<Cliente> listar() { return clienteService.listarTodos(); }

    @GetMapping("/{id}")
    public Cliente obtener(@PathVariable Long id) { return clienteService.buscarPorId(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente crear(@Valid @RequestBody Cliente cliente) { return clienteService.crear(cliente); }

    @PutMapping("/{id}")
    public Cliente actualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) { return clienteService.actualizar(id, cliente); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { clienteService.eliminar(id); }
}