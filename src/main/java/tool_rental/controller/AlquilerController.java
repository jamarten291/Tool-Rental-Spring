package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.dto.AlquilerCreateDTO;
import tool_rental.dto.AlquilerResponseDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Alquiler;
import tool_rental.service.AlquilerService;

import java.util.List;

@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final AlquilerService alquilerService;

    public AlquilerController(AlquilerService alquilerService) { this.alquilerService = alquilerService; }

    @GetMapping
    public List<AlquilerResponseDTO> listar() {
        return EntityDtoMapper.toAlquilerResponseList(alquilerService.listarTodos());
    }

    @GetMapping("/{id}")
    public AlquilerResponseDTO obtener(@PathVariable Long id) {
        Alquiler a = alquilerService.buscarPorId(id);
        return EntityDtoMapper.toDto(a);
    }

    @GetMapping("/cliente/{clienteId}")
    public List<AlquilerResponseDTO> listarPorCliente(@PathVariable Long clienteId) {
        return EntityDtoMapper.toAlquilerResponseList(alquilerService.listarPorCliente(clienteId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AlquilerResponseDTO crear(@Valid @RequestBody AlquilerCreateDTO createDto) {
        Alquiler entidad = EntityDtoMapper.toEntityFromCreateDto(createDto);
        Alquiler creado = alquilerService.crear(entidad);
        return EntityDtoMapper.toDto(creado);
    }

    @PutMapping("/{id}/estado")
    public AlquilerResponseDTO actualizarEstado(@PathVariable Long id, @RequestParam String estado) {
        Alquiler actualizado = alquilerService.actualizarEstado(id, estado);
        return EntityDtoMapper.toDto(actualizado);
    }

    @PostMapping("/{id}/finalizar")
    public void finalizar(@PathVariable Long id) {
        alquilerService.finalizar(id);
    }

    @PostMapping("/{id}/cancelar")
    public AlquilerResponseDTO cancelar(@PathVariable Long id) {
        alquilerService.cancelar(id);
        Alquiler actualizado = alquilerService.buscarPorId(id);
        return EntityDtoMapper.toDto(actualizado);
    }
}