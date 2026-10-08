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
            String token = java.util.UUID.randomUUID().toString();
            usuario.setTokenPassword(token);
            usuarioRepository.save(usuario);

            correoService.enviarCorreoRecuperacion(correo, token);
        }
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
            usuario.setTokenPassword(null);
            usuarioRepository.save(usuario);

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
        session.invalidate();
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario nuevoUsuario) {
        // 1. Validar que el correo no exista
        if (usuarioRepository.findByCorreo(nuevoUsuario.getCorreo()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "mensaje", "El correo ya está registrado."));
        }

        // 2. Limpieza y validación por Rol
        String rol = nuevoUsuario.getRol();

        if ("ALUMNO".equals(rol)) {
            // Validar que la boleta no se duplique
            if (nuevoUsuario.getBoleta() != null && !nuevoUsuario.getBoleta().isEmpty()) {
                if (usuarioRepository.findByBoleta(nuevoUsuario.getBoleta()).isPresent()) {
                    return ResponseEntity.badRequest().body(Map.of("success", false, "mensaje", "La boleta ya está registrada."));
                }
            }
            // Alumnos no tienen número de empleado
            nuevoUsuario.setNumeroEmpleado(null);

        } else if ("DOCENTE".equals(rol) || "EXTERNO".equals(rol)) {
            // Docentes y Externos no tienen boleta ni carrera
            nuevoUsuario.setBoleta(null);
            nuevoUsuario.setCarrera(null);

            if ("EXTERNO".equals(rol)) {
                // Externos tampoco tienen número de empleado
                nuevoUsuario.setNumeroEmpleado(null);
            }
        } else {
            // Evitar inyección de roles extraños desde el front-end
            return ResponseEntity.badRequest().body(Map.of("success", false, "mensaje", "Rol no válido."));
        }

        // 3. Asignación automática de ADMIN al profesor designado
        if ("hacunac@ipn.mx".equals(nuevoUsuario.getCorreo())) {
            nuevoUsuario.setRol("ADMIN");
        } else {
            nuevoUsuario.setRol(rol);
        }

        // 4. Guardar usuario
        usuarioRepository.save(nuevoUsuario);

        return ResponseEntity.ok(Map.of("success", true, "mensaje", "Registro exitoso. Ya puedes iniciar sesión."));
    }
}