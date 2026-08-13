package MediCare.Iniciar.repository;

import MediCare.Iniciar.domain.CitaMedica;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

// Repositorio de citas medicas con las consultas avanzadas que alimentan la vista de consultas
public interface CitaMedicaRepository extends JpaRepository<CitaMedica, Long> {

    // filtra las citas segun su estado activa o inactiva
    List<CitaMedica> findByActivaOrderByFechaAsc(Boolean activa);

    // filtra las citas comprendidas dentro de un rango de fechas
    List<CitaMedica> findByFechaBetweenOrderByFechaAsc(LocalDate fechaInicio, LocalDate fechaFin);

    // coincidencia parcial e insensible a mayusculas sobre la especialidad
    @Query("SELECT c FROM CitaMedica c WHERE LOWER(c.especialidad) LIKE LOWER(CONCAT('%', :textoEspecialidad, '%')) ORDER BY c.especialidad ASC")
    List<CitaMedica> buscarPorEspecialidadParcial(@Param("textoEspecialidad") String textoEspecialidad);

    // total de citas que se encuentran activas en el sistema
    @Query("SELECT COUNT(c) FROM CitaMedica c WHERE c.activa = true")
    long contarCitasActivas();
}
