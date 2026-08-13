package MediCare.Iniciar.repository;

import MediCare.Iniciar.domain.Rol;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

// Repositorio de roles; hereda el CRUD basico de JpaRepository.
public interface RolRepository extends JpaRepository<Rol, Long> {

    // Consulta derivada: evita registrar dos roles con el mismo nombre.
    Optional<Rol> findByNombre(String nombre);
}
