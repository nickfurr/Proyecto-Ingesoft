package com.edu.uniquindio.ruralstay.service;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.Usuario;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía el email de confirmación de reserva (se llama desde ReservaService
     * cuando el propietario acepta y el estado pasa a CONFIRMADA).
     *
     * @param usuario         Usuario/Cliente que realizó la reserva
     * @param reserva         Entidad Reserva del diagrama UML
     * @param montoCancelacion Monto para cancelación (parámetro adicional solicitado)
     */
    public void enviarConfirmacionReserva(Usuario usuario, Reserva reserva, BigDecimal montoCancelacion)
            throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        // Según el diagrama UML, Usuario solo tiene "username".
        // Se asume que username = dirección de email (práctica común).
        // Si se quiere un campo email separado, basta con añadirlo a la clase Usuario.
        helper.setTo(usuario.getEmail());
        helper.setSubject("✅ Confirmación de Reserva #" + reserva.getNumeroReserva());

        String contenidoHtml = construirContenidoHtml(usuario, reserva, montoCancelacion);
        helper.setText(contenidoHtml, true); // true = HTML

        mailSender.send(message);
    }

    public void enviarReservaCreada(Usuario usuario, Reserva reserva) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(usuario.getEmail());
        helper.setSubject("Reserva creada con exito #" + reserva.getNumeroReserva());

        String contenidoHtml = construirContenidoReservaCreada(usuario, reserva);
        helper.setText(contenidoHtml, true);

        mailSender.send(message);
    }

    private String construirContenidoReservaCreada(Usuario usuario, Reserva reserva) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #185fa5;">Hola %s,</h2>
                <p>Tu reserva fue creada correctamente y quedó en estado <strong>%s</strong>.</p>

                <h3>Resumen de la reserva</h3>
                <ul>
                    <li><strong>Número de reserva:</strong> %d</li>
                    <li><strong>Fecha de creación:</strong> %s</li>
                    <li><strong>Fecha de entrada:</strong> %s</li>
                    <li><strong>Fecha de salida:</strong> %s</li>
                    <li><strong>Número de noches:</strong> %d</li>
                    <li><strong>Modalidad:</strong> %s</li>
                    <li><strong>Importe total:</strong> %.2f</li>
                </ul>

                <p>El siguiente paso es completar el pago para avanzar con la reserva.</p>
                <p style="color: #666;">Atentamente,<br>El equipo de RuralStay</p>
            </body>
            </html>
            """,
            usuario.getUsername(),
            reserva.getEstado(),
            reserva.getNumeroReserva(),
            reserva.getFechaReserva().format(formatter),
            reserva.getFechaEntrada().format(formatter),
            reserva.getFechaEntrada().plusDays(reserva.getNumeroNoches()).format(formatter),
            reserva.getNumeroNoches(),
            reserva.getModalidad().name(),
            reserva.getImporteTotal()
        );
    }

    private String construirContenidoHtml(Usuario usuario, Reserva reserva, BigDecimal montoCancelacion) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6;">
                <h2 style="color: #2e7d32;">¡Hola %s!</h2>
                <p>Tu reserva ha sido <strong>aceptada y confirmada</strong> por el propietario.</p>
                
                <h3>Detalles de tu reserva:</h3>
                <ul>
                    <li><strong>Número de Reserva:</strong> %d</li>
                    <li><strong>Fecha de reserva:</strong> %s</li>
                    <li><strong>Fecha de entrada:</strong> %s</li>
                    <li><strong>Número de noches:</strong> %d</li>
                    <li><strong>Modalidad:</strong> %s</li>
                    <li><strong>Importe total:</strong> %.2f €</li>
                </ul>
                
                <p><strong>Información importante sobre cancelación:</strong> El monto asociado a cancelación es de <strong>%.2f €</strong>.</p>
                
                <p>Gracias por elegir nuestra plataforma de casas rurales.</p>
                <p style="color: #666;">Atentamente,<br>El equipo de Alquiler de Casas Rurales</p>
            </body>
            </html>
            """,
            usuario.getUsername(),
            reserva.getNumeroReserva(),
            reserva.getFechaReserva().format(formatter),
            reserva.getFechaEntrada().format(formatter),
            reserva.getNumeroNoches(),
            reserva.getModalidad().name(),
            reserva.getImporteTotal(),
            montoCancelacion
        );
    }
}