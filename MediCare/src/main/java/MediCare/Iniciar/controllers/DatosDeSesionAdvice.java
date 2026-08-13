package MediCare.Iniciar.controllers;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

// Publica el correo y el rol de la sesion hacia todas las vistas para armar la barra de navegacion.
@ControllerAdvice
public class DatosDeSesionAdvice {

    @ModelAttribute("correoEnSesion")
    public String obtenerCorreoEnSesion(Authentication autenticacion) {
        return haySesionIniciada(autenticacion) ? autenticacion.getName() : null;
    }

    @ModelAttribute("rolEnSesion")
    public String obtenerRolEnSesion(Authentication autenticacion) {
        if (!haySesionIniciada(autenticacion)) {
            return null;
        }
        for (GrantedAuthority autoridad : autenticacion.getAuthorities()) {
            // Se devuelve el rol sin el prefijo ROLE_ para poder compararlo en las plantillas.
            return autoridad.getAuthority().replace("ROLE_", "");
        }
        return null;
    }

    private boolean haySesionIniciada(Authentication autenticacion) {
        return autenticacion != null
                && autenticacion.isAuthenticated()
                && !(autenticacion instanceof AnonymousAuthenticationToken);
    }
}
