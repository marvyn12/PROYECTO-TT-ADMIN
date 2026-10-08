package mx.ipn.upiiz.gestiontt.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    // Quitamos el nullable = false porque Docentes y Externos no tendrán boleta
    @Column(unique = true, length = 50)
    private String boleta;

    // Nuevo campo para los Docentes (opcional)
    @Column(length = 50)
    private String numeroEmpleado;

    @Column(nullable = false, unique = true, length = 100)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @Column(length = 50)
    private String carrera; // Solo para alumnos

    @Column(name = "token_password", length = 100)
    private String tokenPassword; // Aquí guardaremos el código temporal

    @Column(nullable = false, length = 20)
    private String rol; // Valores: "ALUMNO", "DOCENTE", "EXTERNO", "ADMIN"
}