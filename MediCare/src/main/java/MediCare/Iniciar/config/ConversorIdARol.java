package MediCare.Iniciar.config;

import MediCare.Iniciar.domain.Rol;
import MediCare.Iniciar.repository.RolRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// Convierte el id que envia la lista desplegable del formulario en la entidad Rol 
@Component
public class ConversorIdARol implements Converter<String, Rol> {

    private final RolRepository rolRepository;

    public ConversorIdARol(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    @Override
    public Rol convert(String idRecibido) {
        if (idRecibido == null || idRecibido.isBlank()) {
            return null;
        }
        return rolRepository.findById(Long.valueOf(idRecibido)).orElse(null);
    }
}
