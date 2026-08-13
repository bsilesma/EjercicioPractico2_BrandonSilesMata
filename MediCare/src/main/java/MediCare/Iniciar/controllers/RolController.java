package MediCare.Iniciar.controllers;

import MediCare.Iniciar.domain.Rol;
import MediCare.Iniciar.service.RolService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador del modulo de roles: atiende el listado, el formulario, el guardado y la eliminacion.
@Controller
@RequestMapping("/roles")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("listaRoles", rolService.obtenerTodos());
        return "roles/listado";
    }

    @GetMapping("/nuevo")
    public String abrirFormularioNuevo(Model modelo) {
        modelo.addAttribute("rol", new Rol());
        return "roles/formulario";
    }

    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicion(@PathVariable Long id, Model modelo, RedirectAttributes redireccion) {
        Rol rolEncontrado = rolService.buscarPorId(id);
        if (rolEncontrado == null) {
            redireccion.addFlashAttribute("mensajeError", "El rol solicitado no existe.");
            return "redirect:/roles";
        }
        modelo.addAttribute("rol", rolEncontrado);
        return "roles/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("rol") Rol rol, Model modelo, RedirectAttributes redireccion) {
        if (rolService.nombreYaRegistrado(rol.getNombre(), rol.getId())) {
            modelo.addAttribute("mensajeError", "Ya existe un rol registrado con ese nombre.");
            return "roles/formulario";
        }
        boolean esNuevo = rol.getId() == null;
        rolService.guardar(rol);
        redireccion.addFlashAttribute("mensajeExito",
                esNuevo ? "El rol se registro correctamente." : "El rol se actualizo correctamente.");
        return "redirect:/roles";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redireccion) {
        try {
            rolService.eliminar(id);
            redireccion.addFlashAttribute("mensajeExito", "El rol se elimino correctamente.");
        } catch (DataIntegrityViolationException excepcion) {
            // La llave foranea de usuario impide borrar un rol que todavia tiene usuarios asignados.
            redireccion.addFlashAttribute("mensajeError",
                    "No se puede eliminar el rol porque hay usuarios asignados a el.");
        }
        return "redirect:/roles";
    }
}
