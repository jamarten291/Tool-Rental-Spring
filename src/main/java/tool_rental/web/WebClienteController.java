package tool_rental.web;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tool_rental.dto.ClienteDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Cliente;
import tool_rental.service.ClienteService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/clientes")
public class WebClienteController {

    private final ClienteService clienteService;

    public WebClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String list(Model model) {
        List<ClienteDTO> clientes = clienteService.listarTodos()
                .stream()
                .map(EntityDtoMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("clientes", clientes);
        return "clientes/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("clienteDto", new ClienteDTO());
        return "clientes/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("clienteDto") ClienteDTO clienteDto,
                          BindingResult bindingResult,
                          RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "clientes/form";
        }
        Cliente entidad = EntityDtoMapper.toEntity(clienteDto);
        Cliente creado = clienteService.crear(entidad);
        ra.addFlashAttribute("success", "Cliente creado");
        return "redirect:/web/clientes";
    }

    @GetMapping("/{id}/editar")
    public String editarForm(@PathVariable Long id, Model model) {
        Cliente c = clienteService.buscarPorId(id);
        model.addAttribute("clienteDto", EntityDtoMapper.toDto(c));
        return "clientes/form";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("clienteDto") ClienteDTO clienteDto,
                             BindingResult bindingResult,
                             RedirectAttributes ra) {
        if (bindingResult.hasErrors()) {
            return "clientes/form";
        }
        Cliente entidad = EntityDtoMapper.toEntity(clienteDto);
        Cliente actualizado = clienteService.actualizar(id, entidad);
        ra.addFlashAttribute("success", "Cliente actualizado");
        return "redirect:/web/clientes";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes ra) {
        clienteService.eliminar(id);
        ra.addFlashAttribute("success", "Cliente eliminado");
        return "redirect:/web/clientes";
    }

    @GetMapping("/{id}/alquileres")
    public String verAlquileresCliente(@PathVariable Long id, Model model) {
        model.addAttribute("clienteId", id);
        model.addAttribute("alquileres", EntityDtoMapper.toAlquilerResponseList(
                // listarPorCliente devuelve entidades Alquiler
                // convertimos a DTOs via mapper
                // usamos servicio directo
                // clienteService no ofrece listarAlquileres, reutilizamos alquilerService via template caller (se inyecta en WebAlquilerController)
                // aquí devolvemos al template un placeholder y la WebAlquilerController aporta la vista específica
                List.of()
        ));
        // Nota: esta ruta básico muestra el enlace. La vista real de alquileres por cliente la proporciona WebAlquilerController (/web/alquileres/cliente/{id})
        return "redirect:/web/alquileres/cliente/" + id;
    }
}