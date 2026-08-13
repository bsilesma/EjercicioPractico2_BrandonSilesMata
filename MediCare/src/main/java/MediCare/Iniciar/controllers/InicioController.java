package MediCare.Iniciar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador de la pagina principal del sitio.
@Controller
public class InicioController {

    @GetMapping("/")
    public String mostrarPaginaPrincipal() {
        return "index";
    }
}
