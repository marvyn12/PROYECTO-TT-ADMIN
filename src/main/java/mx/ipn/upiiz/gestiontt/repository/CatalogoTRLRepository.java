package mx.ipn.upiiz.gestiontt.repository;

import mx.ipn.upiiz.gestiontt.model.CatalogoTRL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatalogoTRLRepository extends JpaRepository<CatalogoTRL, Long> {
    Optional<CatalogoTRL> findByNivel(Integer nivel);
}