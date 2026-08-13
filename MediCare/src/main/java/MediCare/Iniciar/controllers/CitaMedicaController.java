package MediCare.Iniciar.controllers;

import MediCare.Iniciar.domain.CitaMedica;
import MediCare.Iniciar.service.CitaMedicaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador del modulo de citas medicas: listado, detalle, formulario, guardado y eliminacion
@Controller
@RequestMapping("/citas")
public class CitaMedicaController {

    private final CitaMedicaService citaMedicaService;

    public CitaMedicaController(CitaMedicaService citaMedicaService) {
        this.citaMedicaService = citaMedicaService;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("listaCitas", citaMedicaService.obtenerTodas());
        return "citas/listado";
    }

    @GetMapping("/nuevo")
    public String abrirFormularioNuevo(Model modelo) {
        modelo.addAttribute("cita", new CitaMedica());
        return "citas/formulario";
    }

    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicion(@PathVariable Long id, Model modelo, RedirectAttributes redireccion) {
        CitaMedica citaEncontrada = citaMedicaService.buscarPorId(id);
        if (citaEncontrada == null) {
            redireccion.addFlashAttribute("mensajeError", "La cita medica solicitada no existe.");
            return "redirect:/citas";
        }
        modelo.addAttribute("cita", citaEncontrada);
        return "citas/formulario";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model modelo, RedirectAttributes redireccion) {
        CitaMedica citaEncontrada = citaMedicaService.buscarPorId(id);
        if (citaEncontrada == null) {
            redireccion.addFlashAttribute("mensajeError", "La cita medica solicitada no existe.");
            return "redirect:/citas";
        }
        modelo.addAttribute("cita", citaEncontrada);
        return "citas/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("cita") CitaMedica cita, Model modelo, RedirectAttributes redireccion) {
        if (cita.getCosto() != null && cita.getCosto() < 0) {
            modelo.addAttribute("mensajeError", "El costo de la cita no puede ser negativo.");
            return "citas/formulario";
        }

        boolean esRegistroNuevo = cita.getId() == null;
        citaMedicaService.guardar(cita);
        redireccion.addFlashAttribute("mensajeExito",
                esRegistroNuevo ? "La cita medica se registro correctamente."
                        : "La cita medica se actualizo correctamente.");
        return "redirect:/citas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redireccion) {
        citaMedicaService.eliminar(id);
        redireccion.addFlashAttribute("mensajeExito", "La cita medica se elimino correctamente.");
        return "redirect:/citas";
    }
}
