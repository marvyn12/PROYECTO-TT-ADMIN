package mx.ipn.upiiz.gestiontt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "catalogo_trl")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CatalogoTRL {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Integer nivel; // Ej: 1, 2, 3... hasta el 9

    @Column(nullable = false, length = 200)
    private String nombre; // Ej: "Principios básicos observados"

    @Column(columnDefinition = "TEXT")
    private String descripcion; // Lo que se exige para cumplir este nivel
}