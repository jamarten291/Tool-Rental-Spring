package tool_rental.web;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tool_rental.dto.AlquilerCreateDTO;
import tool_rental.dto.AlquilerResponseDTO;
import tool_rental.dto.HerramientaDTO;
import tool_rental.mapper.EntityDtoMapper;
import tool_rental.model.Alquiler;
import tool_rental.model.EstadoAlquiler;
import tool_rental.model.Herramienta;
import tool_rental.service.AlquilerService;
import tool_rental.service.ClienteService;
import tool_rental.service.HerramientaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/web/alquileres")
public class WebAlquilerController {

    private final AlquilerService alquilerService;
    private final ClienteService clienteService;
    private final HerramientaService herramientaService;

    public WebAlquilerController(AlquilerService alquilerService,
                                 ClienteService clienteService,
                                 HerramientaService herramientaService) {
        this.alquilerService = alquilerService;
        this.clienteService = clienteService;
        this.herramientaService = herramientaService;
    }

    @GetMapping
    public String list(Model model) {
        List<AlquilerResponseDTO> alquileres = EntityDtoMapper.toAlquilerResponseList(alquilerService.listarTodos());
        model.addAttribute("alquileres", alquileres);
        return "alquileres/list";
    }

    @GetMapping("/cliente/{clienteId}")
    public String listByCliente(@PathVariable Long clienteId, Model model) {
        List<AlquilerResponseDTO> alquileres = EntityDtoMapper.toAlquilerResponseList(alquilerService.listarPorCliente(clienteId));
        model.addAttribute("alquileres", alquileres);
        model.addAttribute("clienteId", clienteId);
        return "alquileres/list";
    }

    @GetMapping("/nuevo")
    public String nuevoForm(Model model) {
        model.addAttribute("alquilerCreateDto", new AlquilerCreateDTO());
        model.addAttribute("clientes", clienteService.listarTodos().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
        model.addAttribute("herramientas", herramientaService.listarTodos().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
        return "alquileres/create";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid @ModelAttribute("alquilerCreateDto") AlquilerCreateDTO createDto,
                          BindingResult bindingResult,
                          RedirectAttributes ra,
                          Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("clientes", clienteService.listarTodos().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
            model.addAttribute("herramientas", herramientaService.listarTodos().stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
            return "alquileres/create";
        }
        Alquiler entidad = EntityDtoMapper.toEntityFromCreateDto(createDto);
        Alquiler creado = alquilerService.crear(entidad);
        ra.addFlashAttribute("success", "Alquiler creado");
        return "redirect:/web/alquileres";
    }

    @PostMapping("/{id}/cancelar")
    public String cancelar(@PathVariable Long id, RedirectAttributes ra) {
        alquilerService.cancelar(id);
        ra.addFlashAttribute("success", "Alquiler cancelado");
        return "redirect:/web/alquileres";
    }

    @PostMapping("/{id}/finalizar")
    public String finalizar(@PathVariable Long id, RedirectAttributes ra) {
        alquilerService.finalizar(id);
        ra.addFlashAttribute("success", "Alquiler finalizado");
        return "redirect:/web/alquileres";
    }

    @GetMapping("/coste")
    public String calcularCoste(@RequestParam Long herramientaId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIni,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                Model model) {
        Herramienta h = herramientaService.buscarPorId(herramientaId);
        long dias = ChronoUnit.DAYS.between(fechaIni, fechaFin) + 1;
        if (dias < 1) dias = 0;
        BigDecimal precio = h.getPrecioDia() == null ? BigDecimal.ZERO : h.getPrecioDia().multiply(BigDecimal.valueOf(dias));
        model.addAttribute("herramienta", EntityDtoMapper.toDto(h));
        model.addAttribute("fechaIni", fechaIni);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("dias", dias);
        model.addAttribute("coste", precio);
        return "alquileres/coste";
    }

    @GetMapping("/disponibles")
    public String disponiblesForm() {
        return "alquileres/disponibles";
    }

    @PostMapping("/disponibles")
    public String listaDisponibles(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaIni,
                                   @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
                                   Model model) {
        List<Herramienta> herramientas = herramientaService.listarTodos();
        List<Alquiler> alquileresActivos = alquilerService.listarTodos()
                .stream()
                .filter(a -> a.getEstado() == EstadoAlquiler.PENDIENTE || a.getEstado() == EstadoAlquiler.EN_CURSO)
                .collect(Collectors.toList());

        List<Herramienta> disponibles = herramientas.stream().filter(h -> {
            long solapantes = alquileresActivos.stream()
                    .filter(a -> a.getHerramienta() != null && Objects.equals(a.getHerramienta().getId(), h.getId()))
                    .filter(a -> !(a.getFechaFin().isBefore(fechaIni) || a.getFechaIni().isAfter(fechaFin)))
                    .count();
            int stock = h.getStock() == null ? 0 : h.getStock();
            return solapantes < stock;
        }).collect(Collectors.toList());

        model.addAttribute("fechaIni", fechaIni);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("disponibles", disponibles.stream().map(EntityDtoMapper::toDto).collect(Collectors.toList()));
        return "alquileres/disponibles_list";
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        Alquiler a = alquilerService.buscarPorId(id);
        AlquilerResponseDTO dto = EntityDtoMapper.toDto(a);
        model.addAttribute("alquiler", dto);
        return "alquileres/detail";
    }
}