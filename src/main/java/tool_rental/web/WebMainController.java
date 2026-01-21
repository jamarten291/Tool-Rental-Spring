package tool_rental.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador principal de la interfaz web.
 * Mapea el endpoint /web y muestra un menú con enlaces a las operaciones CRUD y reportes.
 */
@Controller
@RequestMapping("/web")
public class WebMainController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("title", "Panel de administración - Tool Rental");
        return "web/index";
    }
}