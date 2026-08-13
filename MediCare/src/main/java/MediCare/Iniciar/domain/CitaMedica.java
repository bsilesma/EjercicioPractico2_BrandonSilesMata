package MediCare.Iniciar.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

// Entidad mapeada a la tabla cita_medica con los datos de la cita y su estado
@Data
@NoArgsConstructor
@Entity
@Table(name = "cita_medica")
public class CitaMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "paciente_nombre", length = 150)
    private String pacienteNombre;

    @Column(name = "especialidad", length = 100)
    private String especialidad;

    @Column(name = "fecha")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fecha;

    @Column(name = "costo")
    private Double costo;

    @Column(name = "activa")
    private Boolean activa;
}
