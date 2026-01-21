package tool_rental.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tool_rental.model.Herramienta;
import tool_rental.service.HerramientaService;

import java.util.List;

@RestController
@RequestMapping("/api/herramientas")
public class HerramientaController {

    private final HerramientaService herramientaService;

    public HerramientaController(HerramientaService herramientaService) { this.herramientaService = herramientaService; }

    @GetMapping
    public List<Herramienta> listar() { return herramientaService.listarTodos(); }

    @GetMapping("/{id}")
    public Herramienta obtener(@PathVariable Long id) { return herramientaService.buscarPorId(id); }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Herramienta crear(@Valid @RequestBody Herramienta herramienta) { return herramientaService.crear(herramienta); }

    @PutMapping("/{id}")
    public Herramienta actualizar(@PathVariable Long id, @Valid @RequestBody Herramienta herramienta) { return herramientaService.actualizar(id, herramienta); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { herramientaService.eliminar(id); }
}