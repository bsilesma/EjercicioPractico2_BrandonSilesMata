package MediCare.Iniciar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

// Envia a cada usuario a su modulo principal apenas inicia sesion, segun el rol que tenga.
@Component
public class RedireccionPorRolHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest peticion, HttpServletResponse respuesta,
            Authentication autenticacion) throws IOException {
        respuesta.sendRedirect(peticion.getContextPath() + resolverDestino(autenticacion));
    }

    private String resolverDestino(Authentication autenticacion) {
        for (GrantedAuthority autoridad : autenticacion.getAuthorities()) {
            switch (autoridad.getAuthority()) {
                case "ROLE_ADMIN":
                    return "/usuarios";
                case "ROLE_MEDICO":
                    return "/citas";
                case "ROLE_PACIENTE":
                    return "/citas";
                default:
                    break;
            }
        }
        return "/";
    }
}
