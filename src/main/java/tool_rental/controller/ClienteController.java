package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.dto.ClienteDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Cliente;
import tool_rental.service.ClienteService;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) { this.clienteService = clienteService; }

    @GetMapping
    public List<ClienteDTO> listar() {
        return EntityDtoMapper.toClienteDtoList(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ClienteDTO obtener(@PathVariable Long id) {
        Cliente c = clienteService.buscarPorId(id);
        return EntityDtoMapper.toDto(c);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteDTO crear(@Valid @RequestBody ClienteDTO clienteDto) {
        Cliente entity = EntityDtoMapper.toEntity(clienteDto);
        Cliente creado = clienteService.crear(entity);
        return EntityDtoMapper.toDto(creado);
    }

    @PutMapping("/{id}")
    public ClienteDTO actualizar(@PathVariable Long id, @Valid @RequestBody ClienteDTO clienteDto) {
        Cliente entity = EntityDtoMapper.toEntity(clienteDto);
        Cliente actualizado = clienteService.actualizar(id, entity);
        return EntityDtoMapper.toDto(actualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { clienteService.eliminar(id); }
}