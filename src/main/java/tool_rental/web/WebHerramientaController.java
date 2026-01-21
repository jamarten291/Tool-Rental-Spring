package tool_rental.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tool_rental.dto.HerramientaDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Herramienta;
import tool_rental.service.HerramientaService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/herramientas")
public class WebHerramientaController {

    private final HerramientaService herramientaService;

    public WebHerramientaController(HerramientaService herramientaService) {
        this.herramientaService = herramientaService;
    }

    @GetMapping
    public String list(Model model) {
        List<HerramientaDTO> herramientas = herramientaService.listarTodos()
                .stream()
                .map(EntityDtoMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("herramientas", herramientas);
        return "herramientas/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("herramientaDto", new HerramientaDTO());
        return "herramientas/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("herramientaDto") HerramientaDTO herramientaDto,
                          BindingResult bindingResult,
                          RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "herramientas/form";
        }
        Herramienta entidad = EntityDtoMapper.toEntity(herramientaDto);
        Herramienta creado = herramientaService.crear(entidad);
        ra.addFlashAttribute("success", "Herramienta creada");
        return "redirect:/web/herramientas";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Herramienta h = herramientaService.buscarPorId(id);
        model.addAttribute("herramientaDto", EntityDtoMapper.toDto(h));
        return "herramientas/form";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("herramientaDto") HerramientaDTO herramientaDto,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "herramientas/form";
        }
        Herramienta entidad = EntityDtoMapper.toEntity(herramientaDto);
        Herramienta actualizado = herramientaService.actualizar(id, entidad);
        ra.addFlashAttribute("success", "Herramienta actualizada");
        return "redirect:/web/herramientas";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        herramientaService.eliminar(id);
        ra.addFlashAttribute("success", "Herramienta eliminada");
        return "redirect:/web/herramientas";
    }
}