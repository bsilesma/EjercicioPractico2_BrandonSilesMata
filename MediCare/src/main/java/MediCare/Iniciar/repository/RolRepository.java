package MediCare.Iniciar.repository;

import MediCare.Iniciar.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio de roles
public interface RolRepository extends JpaRepository<Rol, Long> {

    // evita registrar dos roles con el mismo nombre
    Optional<Rol> findByNombre(String nombre);
}
