package MediCare.Iniciar.controllers;

import MediCare.Iniciar.domain.Rol;
import MediCare.Iniciar.domain.Usuario;
import MediCare.Iniciar.service.RolService;
import MediCare.Iniciar.service.UsuarioService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador del modulo de usuarios: listado, detalle, formulario, guardado y eliminacion
@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final RolService rolService;

    public UsuarioController(UsuarioService usuarioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    // Los roles alimentan la lista desplegable del formulario en todas las vistas del modulo
    @ModelAttribute("listaRoles")
    public List<Rol> cargarRolesDisponibles() {
        return rolService.obtenerTodos();
    }

    @GetMapping
    public String listar(Model modelo) {
        modelo.addAttribute("listaUsuarios", usuarioService.obtenerTodos());
        return "usuarios/listado";
    }

    @GetMapping("/nuevo")
    public String abrirFormularioNuevo(Model modelo) {
        modelo.addAttribute("usuario", new Usuario());
        return "usuarios/formulario";
    }

    @GetMapping("/editar/{id}")
    public String abrirFormularioEdicion(@PathVariable Long id, Model modelo, RedirectAttributes redireccion) {
        Usuario usuarioEncontrado = usuarioService.buscarPorId(id);
        if (usuarioEncontrado == null) {
            redireccion.addFlashAttribute("mensajeError", "El usuario solicitado no existe.");
            return "redirect:/usuarios";
        }
        // La clave no se devuelve al formulario si se deja vacia se conserva la actual
        usuarioEncontrado.setPassword("");
        modelo.addAttribute("usuario", usuarioEncontrado);
        return "usuarios/formulario";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model modelo, RedirectAttributes redireccion) {
        Usuario usuarioEncontrado = usuarioService.buscarPorId(id);
        if (usuarioEncontrado == null) {
            redireccion.addFlashAttribute("mensajeError", "El usuario solicitado no existe.");
            return "redirect:/usuarios";
        }
        modelo.addAttribute("usuario", usuarioEncontrado);
        return "usuarios/detalle";
    }

    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("usuario") Usuario usuario, Model modelo, RedirectAttributes redireccion) {
        if (usuarioService.correoYaRegistrado(usuario.getEmail(), usuario.getId())) {
            modelo.addAttribute("mensajeError", "Ya existe un usuario registrado con ese correo.");
            return "usuarios/formulario";
        }
        if (usuario.getId() == null && (usuario.getPassword() == null || usuario.getPassword().isBlank())) {
            modelo.addAttribute("mensajeError", "La contrasena es obligatoria para un usuario nuevo.");
            return "usuarios/formulario";
        }

        boolean esRegistroNuevo = usuario.getId() == null;
        boolean correoEnviado = usuarioService.guardar(usuario);

        if (!esRegistroNuevo) {
            redireccion.addFlashAttribute("mensajeExito", "El usuario se actualizo correctamente.");
        } else if (correoEnviado) {
            redireccion.addFlashAttribute("mensajeExito",
                    "El usuario se registro y se le envio el correo de bienvenida.");
        } else {
            redireccion.addFlashAttribute("mensajeError",
                    "El usuario se registro, pero no se pudo enviar el correo de bienvenida.");
        }
        return "redirect:/usuarios";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redireccion) {
        usuarioService.eliminar(id);
        redireccion.addFlashAttribute("mensajeExito", "El usuario se elimino correctamente.");
        return "redirect:/usuarios";
    }
}
