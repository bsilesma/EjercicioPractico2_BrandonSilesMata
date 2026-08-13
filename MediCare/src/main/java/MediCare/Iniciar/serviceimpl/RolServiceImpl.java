package MediCare.Iniciar.serviceimpl;

import MediCare.Iniciar.domain.Rol;
import MediCare.Iniciar.repository.RolRepository;
import MediCare.Iniciar.service.RolService;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Implementacion de la logica de negocio de roles apoyada en RolRepository.
@Service
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Rol> obtenerTodos() {
        return rolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Rol buscarPorId(Long id) {
        return rolRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(Rol rol) {
        // El nombre del rol se normaliza en mayusculas para respetar el formato ADMIN, MEDICO y PACIENTE.
        rol.setNombre(rol.getNombre().trim().toUpperCase());
        rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        rolRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean nombreYaRegistrado(String nombre, Long idEnEdicion) {
        Optional<Rol> rolConEseNombre = rolRepository.findByNombre(nombre.trim().toUpperCase());
        if (rolConEseNombre.isEmpty()) {
            return false;
        }
        // Al editar, encontrar el mismo registro no cuenta como duplicado.
        return !rolConEseNombre.get().getId().equals(idEnEdicion);
    }
}
