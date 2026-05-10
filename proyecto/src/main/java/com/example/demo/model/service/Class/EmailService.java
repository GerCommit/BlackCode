package com.example.demo.model.service.Class;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarCorreoRecuperacion(String destino, String token) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("tu_correo_de_prueba@gmail.com"); // Debe coincidir con properties
        mensaje.setTo(destino);
        mensaje.setSubject("Recuperación de Contraseña - Sistema eFact");
        
        // Esta URL apuntará a una pantalla de tu Angular que crearemos después
        String urlDeRecuperacion = "http://localhost:4200/reset-password?token=" + token;
        
        mensaje.setText("Hola,\n\nHas solicitado restablecer tu contraseña.\n"
                + "Haz clic en el siguiente enlace para crear una nueva:\n\n"
                + urlDeRecuperacion + "\n\n"
                + "Si no solicitaste este cambio, ignora este correo.");
        
        mailSender.send(mensaje);
    }

    public void enviarCorreoConfirmacionPedido(String destino, String numeroPedido, Double total, String estado) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setFrom("tu_correo_de_prueba@gmail.com");
        mensaje.setTo(destino);
        mensaje.setSubject("Confirmacion de pedido " + numeroPedido);
        mensaje.setText("Tu pedido fue registrado correctamente.\n"
                + "Numero: " + numeroPedido + "\n"
                + "Total: " + total + "\n"
                + "Estado: " + estado);
        mailSender.send(mensaje);
    }
}
