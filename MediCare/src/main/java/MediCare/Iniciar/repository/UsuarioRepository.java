package MediCare.Iniciar.repository;

import MediCare.Iniciar.domain.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Repositorio de usuarios con las consultas que requieren la autenticacion y la vista de consultas
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Spring Security autentica al usuario por su correo
    Optional<Usuario> findByEmail(String email);

    // lista los usuarios que tienen asignado un rol especifico
    @Query("SELECT u FROM Usuario u WHERE u.rol.nombre = :nombreRol ORDER BY u.nombre ASC")
    List<Usuario> buscarPorNombreDeRol(@Param("nombreRol") String nombreRol);
}
