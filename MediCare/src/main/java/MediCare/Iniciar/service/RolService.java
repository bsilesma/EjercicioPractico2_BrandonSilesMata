package MediCare.Iniciar.service;

import MediCare.Iniciar.domain.Rol;
import java.util.List;

// Contrato de la logica de negocio para la administracion de roles.
public interface RolService {

    List<Rol> obtenerTodos();

    Rol buscarPorId(Long id);

    void guardar(Rol rol);

    void eliminar(Long id);

    // Valida que el nombre no lo tenga ya otro rol distinto al que se esta editando.
    boolean nombreYaRegistrado(String nombre, Long idEnEdicion);
}
