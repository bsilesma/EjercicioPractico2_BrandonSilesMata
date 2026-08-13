package MediCare.Iniciar.service;

import MediCare.Iniciar.domain.CitaMedica;
import java.time.LocalDate;
import java.util.List;

// Contrato de la logica de negocio para la administracion de citas medicas
public interface CitaMedicaService {

    List<CitaMedica> obtenerTodas();

    CitaMedica buscarPorId(Long id);

    void guardar(CitaMedica cita);

    void eliminar(Long id);

    List<CitaMedica> buscarPorEstado(Boolean activa);

    List<CitaMedica> buscarPorRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<CitaMedica> buscarPorEspecialidad(String textoEspecialidad);

    long contarActivas();
}
