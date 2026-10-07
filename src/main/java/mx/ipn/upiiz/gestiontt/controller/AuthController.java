package mx.ipn.upiiz.gestiontt.controller;

import jakarta.servlet.http.HttpSession;
import mx.ipn.upiiz.gestiontt.model.Usuario;
import mx.ipn.upiiz.gestiontt.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private mx.ipn.upiiz.gestiontt.service.CorreoService correoService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("/olvide-password")
    public ResponseEntity<?> olvidePassword(@RequestBody Map<String, String> body) {
        String correo = body.get("correo");
        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            // Generamos un token aleatorio único
            String token = java.util.UUID.randomUUID().toString();
            usuario.setTokenPassword(token);
            usuarioRepository.save(usuario);

            // Enviamos el correo
            correoService.enviarCorreoRecuperacion(correo, token);
        }
        // Siempre decimos que "se envió", por seguridad (para que no adivinen correos)
        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Si el correo existe, se han enviado las instrucciones."));
    }

    @PostMapping("/restablecer-password")
    public ResponseEntity<?> restablecerPassword(@RequestBody Map<String, String> body, HttpSession session) {
        String token = body.get("token");
        String nuevaContrasena = body.get("nuevaContrasena");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByTokenPassword(token);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();
            usuario.setContrasena(nuevaContrasena);
            usuario.setTokenPassword(null); // Borramos el token para que ya no sirva
            usuarioRepository.save(usuario);

            // ¡AQUÍ ESTÁ LA MAGIA! Matamos cualquier sesión que se haya quedado pegada
            session.invalidate();

            return ResponseEntity.ok(Map.of("success", true, "mensaje", "Contraseña actualizada correctamente."));
        }

        return ResponseEntity.badRequest().body(Map.of("success", false, "mensaje", "Enlace inválido o expirado."));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales, HttpSession session) {
        String correo = credenciales.get("correo");
        String contrasena = credenciales.get("contrasena");

        Optional<Usuario> usuarioOpt = usuarioRepository.findByCorreo(correo);

        if (usuarioOpt.isPresent() && usuarioOpt.get().getContrasena().equals(contrasena)) {
            Usuario usuario = usuarioOpt.get();
            session.setAttribute("usuarioLogueado", usuario);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "rol", usuario.getRol(),
                    "mensaje", "Bienvenido " + usuario.getNombre()
            ));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("success", false, "mensaje", "Correo o contraseña incorrectos"));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpSession session) {
        session.invalidate(); // Destruye la sesión
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarAlumno(@RequestBody Usuario nuevoUsuario) {
        // Validar si el correo o boleta ya existen
        if (usuarioRepository.findByCorreo(nuevoUsuario.getCorreo()).isPresent() ||
                usuarioRepository.findByBoleta(nuevoUsuario.getBoleta()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "mensaje", "El correo o la boleta ya están registrados."));
        }

        // Forzar rol ALUMNO
        nuevoUsuario.setRol("ALUMNO");
        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Registro exitoso. Ya puedes iniciar sesión."));
    }
}