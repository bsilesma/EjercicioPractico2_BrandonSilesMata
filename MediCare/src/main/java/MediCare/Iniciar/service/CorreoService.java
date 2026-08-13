package MediCare.Iniciar.service;

import MediCare.Iniciar.domain.Usuario;

// Contrato del envio de correos electronicos del sistema mediante Spring Mail
public interface CorreoService {

    // Devuelve true cuando el correo de bienvenida salio sin problemas hacia el servidor
    boolean enviarCorreoDeBienvenida(Usuario usuario);
}
