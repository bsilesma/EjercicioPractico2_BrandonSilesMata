package MediCare.Iniciar.serviceimpl;

import MediCare.Iniciar.domain.CitaMedica;
import MediCare.Iniciar.repository.CitaMedicaRepository;
import MediCare.Iniciar.service.CitaMedicaService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// Implementacion de la logica de citas medicas apoyada en CitaMedicaRepository.
@Service
public class CitaMedicaServiceImpl implements CitaMedicaService {

    private final CitaMedicaRepository citaMedicaRepository;

    public CitaMedicaServiceImpl(CitaMedicaRepository citaMedicaRepository) {
        this.citaMedicaRepository = citaMedicaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> obtenerTodas() {
        return citaMedicaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaMedica buscarPorId(Long id) {
        return citaMedicaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void guardar(CitaMedica cita) {
        // Una cita sin estado definido se registra como activa.
        if (cita.getActiva() == null) {
            cita.setActiva(Boolean.TRUE);
        }
        citaMedicaRepository.save(cita);
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        citaMedicaRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> buscarPorEstado(Boolean activa) {
        return citaMedicaRepository.findByActivaOrderByFechaAsc(activa);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> buscarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return citaMedicaRepository.findByFechaBetweenOrderByFechaAsc(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaMedica> buscarPorEspecialidad(String textoEspecialidad) {
        return citaMedicaRepository.buscarPorEspecialidadParcial(textoEspecialidad.trim());
    }

    @Override
    @Transactional(readOnly = true)
    public long contarActivas() {
        return citaMedicaRepository.contarCitasActivas();
    }
}
