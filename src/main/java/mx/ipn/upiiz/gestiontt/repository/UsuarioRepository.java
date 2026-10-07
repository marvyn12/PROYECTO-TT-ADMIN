package mx.ipn.upiiz.gestiontt.repository;

import mx.ipn.upiiz.gestiontt.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
    Optional<Usuario> findByBoleta(String boleta);
    Optional<Usuario> findByTokenPassword(String tokenPassword); // <- Agrega esta
}