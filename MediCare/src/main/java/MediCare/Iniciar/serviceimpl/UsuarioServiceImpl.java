package MediCare.Iniciar.serviceimpl;

import MediCare.Iniciar.domain.Usuario;
import MediCare.Iniciar.repository.UsuarioRepository;
import MediCare.Iniciar.service.CorreoService;
import MediCare.Iniciar.service.UsuarioService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Implementacion de la logica de usuarios: encripta las claves y dispara el correo de bienvenida.
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final CorreoService correoService;
    private final PasswordEncoder codificadorClaves;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, CorreoService correoService,
            PasswordEncoder codificadorClaves) {
        this.usuarioRepository = usuarioRepository;
        this.correoService = correoService;
        this.codificadorClaves = codificadorClaves;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public boolean guardar(Usuario usuario) {
        boolean esRegistroNuevo = usuario.getId() == null;

        if (esRegistroNuevo) {
            usuario.setActivo(usuario.getActivo() == null ? Boolean.TRUE : usuario.getActivo());
            usuario.setPassword(codificadorClaves.encode(usuario.getPassword()));
        } else {
            conservarOActualizarClave(usuario);
        }

        usuarioRepository.save(usuario);

        // El correo de bienvenida solo corresponde cuando el usuario se registra por primera vez.
        return esRegistroNuevo && correoService.enviarCorreoDeBienvenida(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean correoYaRegistrado(String email, Long idEnEdicion) {
        Optional<Usuario> usuarioConEseCorreo = usuarioRepository.findByEmail(email.trim());
        if (usuarioConEseCorreo.isEmpty()) {
            return false;
        }
        // Al editar, encontrar el mismo registro no cuenta como duplicado.
        return !usuarioConEseCorreo.get().getId().equals(idEnEdicion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> buscarPorNombreDeRol(String nombreRol) {
        return usuarioRepository.buscarPorNombreDeRol(nombreRol);
    }

    // En una edicion, dejar la clave en blanco significa mantener la que ya estaba guardada.
    private void conservarOActualizarClave(Usuario usuario) {
        if (usuario.getPassword() == null || usuario.getPassword().isBlank()) {
            Usuario usuarioAlmacenado = usuarioRepository.findById(usuario.getId()).orElse(null);
            usuario.setPassword(usuarioAlmacenado == null ? "" : usuarioAlmacenado.getPassword());
        } else {
            usuario.setPassword(codificadorClaves.encode(usuario.getPassword()));
        }
    }
}
