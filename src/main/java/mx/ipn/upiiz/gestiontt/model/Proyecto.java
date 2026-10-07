package mx.ipn.upiiz.gestiontt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String resumen;

    @Column(nullable = false, length = 30)
    private String estado; // "ANTEPROYECTO", "TT1", "TT2"

    @Column(name = "nivel_trl_actual")
    private Integer nivelTrlActual = 1; // Inician en TRL 1 por defecto

    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;

    // Relación One-to-One: Un proyecto pertenece a un solo equipo
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    private Equipo equipo;

    @PrePersist
    protected void onCreate() {
        fechaRegistro = LocalDateTime.now();
    }
}