package mx.ipn.upiiz.gestiontt.repository;

import mx.ipn.upiiz.gestiontt.model.EvidenciaTRL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvidenciaTRLRepository extends JpaRepository<EvidenciaTRL, Long> {
    List<EvidenciaTRL> findByProyectoId(Long proyectoId);
}