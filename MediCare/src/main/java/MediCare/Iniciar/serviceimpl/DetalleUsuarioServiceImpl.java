package MediCare.Iniciar.serviceimpl;

import MediCare.Iniciar.domain.Usuario;
import MediCare.Iniciar.repository.UsuarioRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Traduce un usuario de la base de datos al formato que Spring Security necesita para autenticar.
@Service
public class DetalleUsuarioServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public DetalleUsuarioServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(correo)
                .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con el correo " + correo));

        if (usuarioEncontrado.getRol() == null) {
            throw new UsernameNotFoundException("El usuario " + correo + " no tiene un rol asignado.");
        }

        // Spring Security espera el prefijo ROLE_ para que funcionen hasRole y hasAnyRole.
        return User.builder()
                .username(usuarioEncontrado.getEmail())
                .password(usuarioEncontrado.getPassword())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuarioEncontrado.getRol().getNombre()))
                .disabled(!Boolean.TRUE.equals(usuarioEncontrado.getActivo()))
                .build();
    }
}
