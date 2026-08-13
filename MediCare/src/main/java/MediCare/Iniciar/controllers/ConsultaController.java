package MediCare.Iniciar.controllers;

import MediCare.Iniciar.domain.Rol;
import MediCare.Iniciar.service.CitaMedicaService;
import MediCare.Iniciar.service.RolService;
import MediCare.Iniciar.service.UsuarioService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Controlador de la vista de consultas avanzadas que expone las consultas de los repositorios
@Controller
@RequestMapping("/consultas")
public class ConsultaController {

    private static final String VISTA_CONSULTAS = "consultas/consultas";

    private final CitaMedicaService citaMedicaService;
    private final UsuarioService usuarioService;
    private final RolService rolService;

    public ConsultaController(CitaMedicaService citaMedicaService, UsuarioService usuarioService,
            RolService rolService) {
        this.citaMedicaService = citaMedicaService;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
    }

    // Estos datos se muestran en la pagina sin importar cual consulta se ejecute
    @ModelAttribute("totalCitasActivas")
    public long cargarTotalDeCitasActivas() {
        return citaMedicaService.contarActivas();
    }

    @ModelAttribute("listaRoles")
    public List<Rol> cargarRolesDisponibles() {
        return rolService.obtenerTodos();
    }

    @GetMapping
    public String abrirPanelDeConsultas() {
        return VISTA_CONSULTAS;
    }

    @GetMapping("/estado")
    public String consultarPorEstado(@RequestParam("estadoCita") Boolean estadoCita, Model modelo) {
        modelo.addAttribute("citasEncontradas", citaMedicaService.buscarPorEstado(estadoCita));
        modelo.addAttribute("tituloResultado", estadoCita ? "Citas medicas activas" : "Citas medicas inactivas");
        modelo.addAttribute("estadoCita", estadoCita);
        return VISTA_CONSULTAS;
    }

    @GetMapping("/rango-fechas")
    public String consultarPorRangoDeFechas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model modelo) {

        modelo.addAttribute("fechaInicio", fechaInicio);
        modelo.addAttribute("fechaFin", fechaFin);

        if (fechaFin.isBefore(fechaInicio)) {
            modelo.addAttribute("mensajeError", "La fecha final no puede ser anterior a la fecha inicial.");
            return VISTA_CONSULTAS;
        }

        modelo.addAttribute("citasEncontradas", citaMedicaService.buscarPorRangoDeFechas(fechaInicio, fechaFin));
        modelo.addAttribute("tituloResultado", "Citas medicas entre " + fechaInicio + " y " + fechaFin);
        return VISTA_CONSULTAS;
    }

    @GetMapping("/especialidad")
    public String consultarPorEspecialidad(@RequestParam("textoEspecialidad") String textoEspecialidad, Model modelo) {
        modelo.addAttribute("citasEncontradas", citaMedicaService.buscarPorEspecialidad(textoEspecialidad));
        modelo.addAttribute("tituloResultado", "Citas medicas con especialidad que contiene: " + textoEspecialidad);
        modelo.addAttribute("textoEspecialidad", textoEspecialidad);
        return VISTA_CONSULTAS;
    }

    @GetMapping("/usuarios-por-rol")
    public String consultarUsuariosPorRol(@RequestParam("nombreRol") String nombreRol, Model modelo) {
        modelo.addAttribute("usuariosEncontrados", usuarioService.buscarPorNombreDeRol(nombreRol));
        modelo.addAttribute("tituloResultado", "Usuarios con el rol " + nombreRol);
        modelo.addAttribute("nombreRol", nombreRol);
        return VISTA_CONSULTAS;
    }
}
