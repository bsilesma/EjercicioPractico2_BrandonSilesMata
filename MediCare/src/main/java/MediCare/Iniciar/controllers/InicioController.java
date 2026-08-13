package MediCare.Iniciar.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// Controlador de las paginas generales del sitio: inicio, login y acceso denegado.
@Controller
public class InicioController {

    @GetMapping("/")
    public String mostrarPaginaPrincipal() {
        return "index";
    }

    @GetMapping("/login")
    public String mostrarFormularioDeAcceso() {
        return "login";
    }

    @GetMapping("/acceso-denegado")
    public String mostrarAccesoDenegado() {
        return "acceso-denegado";
    }
}
