package com.votacion.auth_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl  implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarCodigo(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("Tu código de votación");
        mensaje.setText("¡Hola! Tu código de votación es: " + codigo);
        mensaje.setFrom("tu-correo@gmail.com");

        mailSender.send(mensaje);
    }
}

