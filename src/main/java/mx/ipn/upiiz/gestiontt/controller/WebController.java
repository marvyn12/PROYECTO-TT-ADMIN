package mx.ipn.upiiz.gestiontt.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    // Ruta por default (Raíz)
    @GetMapping("/")
    public String index(HttpSession session) {
        // Si no hay un usuario en sesión, patéalo al login por default
        if (session.getAttribute("usuarioLogueado") == null) {
            return "redirect:/login";
        }
        // Si ya está logueado, lo deja pasar al dashboard principal
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        // Si intenta ir al login pero ya inició sesión, regrésalo al inicio
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/recuperar")
    public String recuperarPage() {
        return "recuperar";
    }

    @GetMapping("/restablecer")
    public String restablecerPage(@org.springframework.web.bind.annotation.RequestParam("token") String token, org.springframework.ui.Model model) {
        // Le pasamos el token al HTML para saber a quién le cambiamos la contraseña
        model.addAttribute("token", token);
        return "restablecer";
    }

    @GetMapping("/registro")
    public String registro(HttpSession session) {
        // Si intenta ir al registro pero ya inició sesión, regrésalo al inicio
        if (session.getAttribute("usuarioLogueado") != null) {
            return "redirect:/";
        }
        return "registro";
    }
}