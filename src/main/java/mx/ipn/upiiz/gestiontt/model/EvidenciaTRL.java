package mx.ipn.upiiz.gestiontt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "evidencias_trl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvidenciaTRL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalogo_trl_id", nullable = false)
    private CatalogoTRL nivelTrl;

    @Column(name = "texto_alumno", columnDefinition = "TEXT")
    private String textoAlumno; // Aquí cae el texto del Anteproyecto/TT1/TT2

    @Column(name = "ruta_archivo", length = 255)
    private String rutaArchivo; // Si en lugar de texto suben un PDF/Word

    @Column(name = "fecha_subida")
    private LocalDateTime fechaSubida;

    @PrePersist
    protected void onCreate() {
        fechaSubida = LocalDateTime.now();
    }
}