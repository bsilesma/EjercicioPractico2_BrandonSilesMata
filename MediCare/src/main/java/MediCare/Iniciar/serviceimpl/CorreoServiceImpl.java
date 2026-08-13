package MediCare.Iniciar.serviceimpl;

import MediCare.Iniciar.domain.Usuario;
import MediCare.Iniciar.service.CorreoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

// Implementacion del envio de correos con Spring Mail sobre el servidor SMTP configurado.
@Service
public class CorreoServiceImpl implements CorreoService {

    private static final Logger BITACORA = LoggerFactory.getLogger(CorreoServiceImpl.class);

    private final JavaMailSender emisorDeCorreos;

    @Value("${spring.mail.username}")
    private String cuentaRemitente;

    public CorreoServiceImpl(JavaMailSender emisorDeCorreos) {
        this.emisorDeCorreos = emisorDeCorreos;
    }

    @Override
    public boolean enviarCorreoDeBienvenida(Usuario usuario) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom(cuentaRemitente);
        mensaje.setTo(usuario.getEmail());
        mensaje.setSubject("Bienvenido a MediCare");
        mensaje.setText(armarCuerpoDeBienvenida(usuario));

        try {
            emisorDeCorreos.send(mensaje);
            return true;
        } catch (MailException excepcion) {
            // Si el SMTP falla se deja constancia en la bitacora, pero el registro del usuario no se pierde.
            BITACORA.error("No se pudo enviar el correo de bienvenida a {}", usuario.getEmail(), excepcion);
            return false;
        }
    }

    private String armarCuerpoDeBienvenida(Usuario usuario) {
        return "Estimado(a) " + usuario.getNombre() + ","
                + System.lineSeparator() + System.lineSeparator()
                + "Su cuenta en la plataforma MediCare fue creada correctamente."
                + System.lineSeparator()
                + "Correo de acceso: " + usuario.getEmail()
                + System.lineSeparator()
                + "Rol asignado: " + (usuario.getRol() != null ? usuario.getRol().getNombre() : "sin asignar")
                + System.lineSeparator() + System.lineSeparator()
                + "MediCare, servicios de salud.";
    }
}
