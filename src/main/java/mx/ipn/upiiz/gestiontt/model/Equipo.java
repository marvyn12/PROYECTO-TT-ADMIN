package mx.ipn.upiiz.gestiontt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_equipo", nullable = false, unique = true)
    private String numeroEquipo; // Ej: "TT-2026-A1"

    // Relación One-to-Many: Un equipo tiene muchos alumnos (usuarios con rol ALUMNO)
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id")
    private List<Usuario> integrantes = new ArrayList<>();

    // El profesor que registró o evalúa a este equipo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id")
    private Usuario profesorAsignado;
}