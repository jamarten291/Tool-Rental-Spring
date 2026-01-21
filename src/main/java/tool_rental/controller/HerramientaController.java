package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.dto.HerramientaDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Herramienta;
import tool_rental.service.HerramientaService;

import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
public class HerramientaController {

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) { this.herramientaService = herramientaService; }

    @GetMapping
    public List<HerramientaDTO> listar() {
        return EntityDtoMapper.toHerramientaDtoList(herramientaService.listarTodos());
    }

    @GetMapping("/{id}")
    public HerramientaDTO obtener(@PathVariable Long id) {
        Herramienta h = herramientaService.buscarPorId(id);
        return EntityDtoMapper.toDto(h);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HerramientaDTO crear(@Valid @RequestBody HerramientaDTO herramientaDto) {
        Herramienta entity = EntityDtoMapper.toEntity(herramientaDto);
        Herramienta creado = herramientaService.crear(entity);
        return EntityDtoMapper.toDto(creado);
    }

    @PutMapping("/{id}")
    public HerramientaDTO actualizar(@PathVariable Long id, @Valid @RequestBody HerramientaDTO herramientaDto) {
        Herramienta entity = EntityDtoMapper.toEntity(herramientaDto);
        Herramienta actualizado = herramientaService.actualizar(id, entity);
        return EntityDtoMapper.toDto(actualizado);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { herramientaService.eliminar(id); }
}