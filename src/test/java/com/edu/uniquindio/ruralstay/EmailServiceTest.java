package com.edu.uniquindio.ruralstay;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.util.Assert;

import com.edu.uniquindio.ruralstay.entity.Cliente;
import com.edu.uniquindio.ruralstay.entity.Pago;
import com.edu.uniquindio.ruralstay.entity.Reserva;
import com.edu.uniquindio.ruralstay.entity.Usuario;
import com.edu.uniquindio.ruralstay.entity.enums.ModalidadAlquiler;
import com.edu.uniquindio.ruralstay.service.EmailService;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;


@SpringBootTest
public class EmailServiceTest {
    

    private static GreenMail greenMail;
    private static Usuario usuario;
    private static Reserva reserva;
    private static Pago pago;
    @Autowired
    private EmailService emailService;

    @BeforeAll
    static void startServer() {
        greenMail = new GreenMail(new ServerSetup(3025, null, "smtp"));

        pago = new Pago();

        reserva = new Reserva();
        reserva.setFechaReserva(LocalDate.now());
        reserva.setFechaEntrada(LocalDate.of(2026, 5, 10));
        reserva.setNumeroNoches(5);
        reserva.setModalidad(ModalidadAlquiler.AMBAS);
        reserva.setImporteTotal(new BigDecimal("1000.01232"));
        reserva.setPago(pago);
        reserva.setNumeroReserva(12345L);
       
        usuario = new Cliente();
        usuario.setEmail("juana.betancourtp@uqvirtual.edu.com");
    
        greenMail.start();
    }

    @AfterAll
    static void stopServer() {
        greenMail.stop();
    }

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.mail.host", () -> "localhost");
        registry.add("spring.mail.port", () -> 3025);
        registry.add("spring.mail.properties.mail.smtp.auth", () -> false);
        registry.add("spring.mail.properties.mail.smtp.starttls.enable", () -> false);

    }

    @Test
    public void testEnviarConfirmacionReservaUsuarioCorrecto() throws Exception {
        // Aquí se debería crear una Reserva de prueba y un monto de cancelación
        // Luego se llamaría a emailService.enviarConfirmacionReserva(usuario, reserva, montoCancelacion)
        // Y se verificaría que el email se envió correctamente
        
        emailService.enviarConfirmacionReserva(usuario, reserva, new BigDecimal("200.00"));

        greenMail.waitForIncomingEmail(1);

        MimeMessage[] messages = greenMail.getReceivedMessages();
        assertEquals(1, messages.length);

        MimeMessage message = messages[0];

        assertEquals("juana.betancourtp@uqvirtual.edu.com",
                    message.getAllRecipients()[0].toString());
    }
    
}
