package MediCare.Iniciar.service;

import MediCare.Iniciar.domain.Usuario;
import java.util.List;

// Contrato de la logica de negocio para la administracion de usuarios
public interface UsuarioService {

    List<Usuario> obtenerTodos();

    Usuario buscarPorId(Long id);

    // Guarda el usuario y devuelve true si ademas se envio el correo de bienvenida
    boolean guardar(Usuario usuario);

    void eliminar(Long id);

    // Valida que el correo no lo tenga ya otro usuario distinto al que se esta editando
    boolean correoYaRegistrado(String email, Long idEnEdicion);

    List<Usuario> buscarPorNombreDeRol(String nombreRol);
}
