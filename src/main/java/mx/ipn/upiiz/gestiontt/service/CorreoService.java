package mx.ipn.upiiz.gestiontt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class CorreoService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCorreoRecuperacion(String destino, String token) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destino);
        mensaje.setSubject("Recuperación de contraseña - UPIIZ");

        // El enlace que recibirá el alumno (apunta a nuestro WebController)
        String url = "http://localhost:8081/restablecer?token=" + token;

        mensaje.setText("Hola,\n\n"
                + "Hemos recibido una solicitud para restablecer tu contraseña en el Sistema TT.\n"
                + "Haz clic en el siguiente enlace para crear una nueva:\n\n"
                + url + "\n\n"
                + "Si no solicitaste esto, ignora este correo.");

        mailSender.send(mensaje);
    }
}