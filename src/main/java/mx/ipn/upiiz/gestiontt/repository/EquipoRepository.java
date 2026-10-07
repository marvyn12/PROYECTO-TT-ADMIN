package mx.ipn.upiiz.gestiontt.repository;

import mx.ipn.upiiz.gestiontt.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {
    Optional<Equipo> findByNumeroEquipo(String numeroEquipo);
}