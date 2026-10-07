package mx.ipn.upiiz.gestiontt.repository;

import mx.ipn.upiiz.gestiontt.model.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
    List<Proyecto> findByEstado(String estado);
}